# Medical Supplies Backend

## Requirements

- .NET Core >= 3.1
- Docker >= 19.0 (only development)

## Setup

## Build

```
dotnet build
```

## Run

```
dotnet run
```

The app will be available at http://localhost:5000

## create and run with docker

```
docker build -t netcoreapp .
```

```
docker run --rm -it \
 -p 8080:80 \
 netcoreapp
```