# QR Code Service

1. Build the container image on Cloud Build using Buildpacks, storing the image on Google Container Registry:
    ```
    export PROJECT_ID=YOUR_GCP_PROJECT
    gcloud builds submit --pack=image=gcr.io/$PROJECT_ID/qr-code-service
    ```

2. Deploy the container on Cloud Run:
    ```
    gcloud run deploy \
      --project=$PROJECT_ID \
      --region=asia-northeast1 \
      --platform=managed \
      --allow-unauthenticated \
      --image=gcr.io/$PROJECT_ID/qr-code-service \
      qr-code-service
    ```