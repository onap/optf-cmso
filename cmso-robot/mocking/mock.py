from flask import Flask
from flask import request
from flask import Response
from flask import json
from flask import send_from_directory
import requests
from threading import Thread
import time

import os
import fnmatch
import re
import time
import datetime

app = Flask(__name__)
ROOT_MOCK_DIR = os.path.dirname(os.path.abspath(__file__))
DATA_DIR =   os.path.join(ROOT_MOCK_DIR, "data") 
global requestNum
requestNum = 1

########################################################################
########################################################################
@app.route('/onap/so/infra/orchestrationRequests/v7/schedule/<VNFNAME>', methods=['GET', 'POST'])
def soSchedule(VNFNAME):
    if request.method == 'POST':
        testid = request.headers.environ["HTTP_X_TRANSACTIONID"]
        response = {
            "status" : "202",
            "entity" : {
                "requestReferences" : {
                    "requestId" : "000001"
                    }
                }
            }
        resp = Response(json.dumps(response), 200, mimetype='application/json')
        return resp           

        
    else :
        return "Helloooooo!!!!"

########################################################################
########################################################################
@app.route('/onap/so/infra/orchestrationRequests/v7/<REQUESTID>', methods=['GET'])
def soStatus(REQUESTID):
    response = {"request" : { "requestStatus" : {
        "requestState" : "COMPLETE",
        "statusMessage" : "Done.",
        "percentProgress" : 100,
        "finishTime" : ""
        }}}
    now = datetime.datetime.utcnow()
    #response["finishTime"] = now.strftime("%Y-%m-%dT%H:%M:%SZ")
    response["request"]["requestStatus"]["finishTime"] = now.strftime("%a, %d %b %Y %H:%M:%S GMT")
    resp = Response(json.dumps(response), 200, mimetype='application/json')
    
    return resp           


########################################################################
########################################################################
@app.route('/optimizer/v1/optimize/schedule', methods=['POST'])
def optimizePost():
    response = {}
    resp = Response(json.dumps(response), 200, mimetype='application/json')
    return resp           


########################################################################
########################################################################
@app.route('/optimizer/v1/optimize/schedule/<ID>', methods=['GET'])
def optimizeGet(ID):
    response = {}
    resp = Response(json.dumps(response), 200, mimetype='application/json')
    
    return resp           

########################################################################
########################################################################
@app.route('/optimizer/v1/optimize/schedule/<ID>', methods=['DELETE'])
def optimizeDelete(ID):
    response = {}
    resp = Response(json.dumps(response), 200, mimetype='application/json')
    return resp           

########################################################################
########################################################################
@app.route('/optimizer/v1/policies', methods=['GET'])
def getPolicies():
    reponse = []
    resp = Response(json.dumps(response), 200, mimetype='application/json')
    
    return resp           

########################################################################
########################################################################
if __name__ == "__main__":
    app.run(host= '0.0.0.0',port=5000)
    #app.run()