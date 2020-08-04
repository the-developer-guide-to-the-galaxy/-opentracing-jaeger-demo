using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using OpenTracing;

namespace NetCoreApp.Controllers
{
    [ApiController]
    [Route("")]
    public class DefaultController : ControllerBase
    {
        private readonly ILogger<DefaultController> _logger;
        private readonly ITracer _tracer;

        public DefaultController(
            ILogger<DefaultController> logger,
            ITracer tracer
        )
        {
            _logger = logger;
            _tracer = tracer;
        }

        [HttpGet]
        [Route("random")]
        public RandomResponseDTO Random()
        { 
            var operationName = "GET::random";
            var builder = _tracer.BuildSpan(operationName);
            using (var scope = builder.StartActive(true))
            {
                Random random = new Random();
                int value = random.Next();
                _logger.LogInformation("random: " + value);
                RandomResponseDTO randomResponseDTO = new RandomResponseDTO{
                    Value = value
                };
                return randomResponseDTO;
            }
        }

        [HttpGet]
        [Route("hang-up-random")]
        public RandomResponseDTO HangUpRandom()
        { 
            var operationName = "GET::hang-up-random";
            var builder = _tracer.BuildSpan(operationName);
            using (var scope = builder.StartActive(true))
            {
                Task.Delay(30000).Wait();
                Random random = new Random();
                int value = random.Next();
                
                _logger.LogInformation("random: " + value);
                RandomResponseDTO randomResponseDTO = new RandomResponseDTO{
                    Value = value
                };
                return randomResponseDTO;
            }
        }
    }

    public class RandomResponseDTO{
        public int Value {get; set;}
    }
}
