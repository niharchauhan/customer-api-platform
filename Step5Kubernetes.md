# Step 5: Kubernetes Deployment Guide

This document outlines the process to prepare and deploy the `customer-api-service` Spring Boot application into a Kubernetes environment using **Minikube**.

---

## Kubernetes Resources

The following Kubernetes manifests are created inside the `k8s/` directory:

- `configmap.yaml`: Application configuration values
- `deployment.yaml`: Defines the pod and deployment strategy
- `service.yaml`: Exposes the application via NodePort

---

## Install Minikube on macOS ARM x64

If Minikube is not already installed, follow these steps:

1. **Download the Minikube binary**
   ```bash
   curl -LO https://github.com/kubernetes/minikube/releases/download/v1.36.0/minikube-darwin-arm64
   ```

2. **Make the file executable**
   ```bash
   chmod +x minikube-darwin-arm64
   ```

3. **Move it to `/usr/local/bin` (to make it globally accessible)**
   ```bash
   sudo mv minikube-darwin-arm64 /usr/local/bin/minikube
   ```

4. **Verify the installation**
   ```bash
   minikube version
   ```

---

## Install kubectl on macOS ARM x64

If kubectl is not already installed, run:

```bash
brew install kubectl
```

### Verify installation

```bash
kubectl version --client
```

You should see output similar to:

```
Client Version: v1.32.2
Kustomize Version: v5.5.0
```

---

## Setup Instructions

### 1. Start Minikube - Minikube has its own Docker daemon, so run:

```bash
minikube start
```

### 2. Run this so Docker builds happen inside Minikube (required for Kubernetes to see your image):

```bash
eval $(minikube docker-env)
```

```bash
cd customer-api-service
```

### 3. Now when you run:

```bash
docker build -t customer-api-service .
```

It builds inside the Minikube environment and will be accessible to your K8s deployment.

## Deploy to Kubernetes

### 1. Apply Kubernetes Resources

```bash
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

### 2. Verify Resources

```bash
kubectl get pods
```

### 3. Access the Service

Run:

```bash
minikube service customer-api-service
```

This will expose the application via a NodePort and open it in your default browser.

---

## Configuration Inputs

If using a local MySQL database, the following `ConfigMap` values must be accurate:

- `DB_HOST=host.minikube.internal`
- `DB_PORT=3306`
- `DB_NAME=customer_db`
- `DB_USER=root`
- `DB_PASSWORD=`

Edit `k8s/configmap.yaml` accordingly.

---

## Extending to Cloud Clusters

Moving from Minikube to Production-Grade Clusters (EKS, GKE, AKS)

- **Replace `NodePort` with `LoadBalancer` or Ingress** -> In cloud environments, use the LoadBalancer service type instead of NodePort to automatically assign a public IP and expose your service externally. This simplifies access for users and supports scalable traffic routing. Alternatively, you can use an Ingress controller to manage multiple routes and enable TLS (HTTPS) termination.

- **Use Kubernetes Secrets (Not ConfigMaps) for Sensitive Data** -> In cloud deployments, sensitive data (e.g., DB credentials, API keys) should be stored in Kubernetes Secrets, not hard-coded in application.properties or ConfigMaps. Secrets are base64-encoded and can be integrated with cloud secret managers (e.g., AWS Secrets Manager, GCP Secret Manager). ConfigMaps are still useful for non-sensitive configuration like environment names or feature flags.

- **Use Persistent Volume Claims (PVCs) for Data** -> If your application needs to store data persistently, e.g., uploaded files or logs, use PVCs to request storage from your cloud provider (e.g., EBS in AWS, Persistent Disks in GCP). This ensures that your data is not lost when containers are restarted or rescheduled. Each cloud platform supports dynamic provisioning of volumes, so setup is seamless.

- **Use Helm charts for parameterized deployments** -> Helm help automate and manage Kubernetes manifests by allowing you to template values and organize configuration for different environments. This is especially helpful in production setups where configurations may vary across dev, staging, and prod. Helm also provides easy version control and rollback for deployments.

---
