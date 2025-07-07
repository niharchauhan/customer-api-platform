
# CI/CD Pipeline Documentation

This document outlines the CI/CD pipeline for the `customer-api-service` project using GitHub Actions.

## Overview

The pipeline performs the following steps:
1. **Build and Test**: Compiles the application using Maven and runs all tests.
2. **Docker Build**: Builds a Docker image of the application after successful build and tests.
3. **Deployment**: Applies the Kubernetes configuration to deploy the application if the push was to the `main` branch.

## Trigger

The pipeline is triggered on:
- Any push to the `main` branch
- Any pull request targeting the `main` branch

## Jobs

### 1. Build and Test

- **Runs on**: `ubuntu-latest`
- **Steps**:
  - Check out the repository code
  - Set up JDK 21 using Temurin distribution
  - Build the project and run tests using Maven

### 2. Docker Build

- **Runs on**: `ubuntu-latest`
- **Depends on**: `build-and-test`
- **Steps**:
  - Check out the repository code
  - Build Docker image tagged as `customer-api-service:latest`

### 3. Deploy

- **Runs on**: `ubuntu-latest`
- **Depends on**: `docker-build`
- **Condition**: Only runs when changes are pushed to the `main` branch
- **Steps**:
  - Apply Kubernetes configurations:
    - `k8s/configmap.yaml`
    - `k8s/deployment.yaml`
    - `k8s/service.yaml`


## Automated Gates

Our pipeline uses the following gates to control the flow of deployments and ensure only verified code is promoted:

- **Test Pass Gate**:  
  The `build-and-test` job must successfully complete before Docker image building (`docker-build`) can start. This ensures no image is built from untested or failing code.

- **Branch Gate**:  
  The `deploy` job runs **only** if the commit is pushed to the `main` branch. This prevents accidental deployments from feature branches.

- **Job Dependency Gate**:  
  `docker-build` and `deploy` jobs depend on the success of earlier jobs using the `needs:` keyword to enforce proper sequencing.