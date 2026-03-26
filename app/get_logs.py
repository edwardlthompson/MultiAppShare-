import requests
import json
import sys

# --- Configuration ---
GITLAB_URL = "https://gitlab.com"
PROJECT_ID = "edwardleethompson/fdroiddata" # Your project path
PIPELINE_ID = "2409866605"
PRIVATE_TOKEN = "glpat-FQ4-U7Bd9iOe5NH12WUyEWM6MQpvOjEKdToxOWc1Ng8.01.170n15fe9"

headers = {"PRIVATE-TOKEN": PRIVATE_TOKEN}

def get_failed_logs():
    try:
        # 1. Get all jobs for the pipeline
        jobs_url = f"{GITLAB_URL}/api/v4/projects/{PROJECT_ID.replace('/', '%2F')}/pipelines/{PIPELINE_ID}/jobs"
        response = requests.get(jobs_url, headers=headers)
        response.raise_for_status()
        jobs = response.json()

        # 2. Filter for failed jobs
        failed_jobs = [j for j in jobs if j['status'] == 'failed']
        
        print(f"--- Found {len(failed_jobs)} failures in Pipeline {PIPELINE_ID} ---\n")

        for job in failed_jobs:
            print(f"=== JOB: {job['name']} (ID: {job['id']}) ===")
            # 3. Get the raw log (trace) for each failed job
            trace_url = f"{GITLAB_URL}/api/v4/projects/{PROJECT_ID.replace('/', '%2F')}/jobs/{job['id']}/trace"
            trace_res = requests.get(trace_url, headers=headers)
            
            if trace_res.status_code == 200:
                # We take the last 3000 characters to focus on the error tail
                print(trace_res.text[-3000:]) 
            else:
                print(f"Could not retrieve log. Status: {trace_res.status_code}")
            print("\n" + "="*40 + "\n")
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    get_failed_logs()
