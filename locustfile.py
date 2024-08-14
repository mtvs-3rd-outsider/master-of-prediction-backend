from locust import HttpUser, task, between
import requests
from requests.packages.urllib3.exceptions import InsecureRequestWarning

# Suppress only the single InsecureRequestWarning from urllib3 needed to bypass SSL verification
requests.packages.urllib3.disable_warnings(InsecureRequestWarning)

class MyUser(HttpUser):
    wait_time = between(1, 5)
    host = "https://master-of-prediction.shop:8081"

    @task
    def index(self):
        self.client.get("/", verify=False)

    @task
    def betting(self):
        self.client.get("/betting/46", verify=False)
