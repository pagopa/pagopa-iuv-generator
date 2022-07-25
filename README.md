# pagoPA IUV Generator function

## Api Documentation 📖

See the [OpenApi 3 here.](https://editor.swagger.io/?url=https://raw.githubusercontent.com/pagopa/pagopa-iuv-generator/main/openapi/openapi.json)

---

## Run locally with Docker
`docker build -t pagopa-functions-template .`

`docker run -p 8999:80 pagopa-functions-template`

### Test
`curl http://localhost:8999/example`

## Run locally with Maven

`mvn clean package`

`mvn azure-functions:run`

### Test
`curl http://localhost:7071/example` 

---


## TODO
Once cloned the repo, you should configure the following GitHub action in `.github` folder: 
- `deploy.yml`
- `sonar_analysis.yml`

and pipeline configuration in `.devops` folder:
- `code-review-pipelines.yaml`
- `deploy-pipelines.yaml`

Configure the SonarCloud project :point_right: [guide](https://pagopa.atlassian.net/wiki/spaces/DEVOPS/pages/147193860/SonarCloud+experimental).
