using System;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using OpenTracing.Util;
using Jaeger.Reporters;
using Jaeger.Samplers;

namespace NetCoreApp
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            string JAEGER_SERVICE_NAME = Environment.GetEnvironmentVariable("JAEGER_SERVICE_NAME");
            string JAEGER_AGENT_PORT   = Environment.GetEnvironmentVariable("JAEGER_AGENT_PORT");
            string JAEGER_AGENT_HOST   = Environment.GetEnvironmentVariable("JAEGER_AGENT_HOST");
            services.AddOpenTracing();
            var sender = new Jaeger.Senders.Thrift.UdpSender(
                JAEGER_AGENT_HOST, 
                int.Parse(JAEGER_AGENT_PORT), 
                0
            );
            var remoteReporter = new RemoteReporter.Builder()
                .WithSender(sender)
                .Build();
            var sampler = new ConstSampler(true);
            var tracer = new Jaeger.Tracer.Builder(JAEGER_SERVICE_NAME)
                .WithReporter(remoteReporter)
                .WithSampler(sampler)
                .Build();
            GlobalTracer.Register(tracer);
            services.AddHttpClient();
            services.AddControllers();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            app.UseRouting();
            app.UseCors();
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
