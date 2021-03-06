from os import listdir
from os.path import isfile, join

# Should be provided in Jenkins job

GLOBAL_SCHEDULER_URL = "http://127.0.0.1:8080"
GLOBAL_SCHEDULER_USER = "oof@oof.onap.org"
GLOBAL_SCHEDULER_PASSWORD = "demo123456!"

GLOBAL_OPTIMIZER_URL = "http://127.0.0.1:7997"
GLOBAL_OPTIMIZER_USER = "oof@oof.onap.org"
GLOBAL_OPTIMIZER_PASSWORD = "demo123456!"

GLOBAL_TICKET_MGT_URL = "http://127.0.0.1:7999"
GLOBAL_TICKET_MGT_USER = "oof@oof.onap.org"
GLOBAL_TICKET_MGT_PASSWORD = "demo123456!"

GLOBAL_TOPOLOGY_URL = "http://127.0.0.1:7998"
GLOBAL_TOPOLOGY_USER = "oof@oof.onap.org"
GLOBAL_TOPOLOGY_PASSWORD = "demo123456!"


GLOBAL_CALLBACK_USERID = "onap-user"
GLOBAL_CALLBACK_PASSWORD = "onap-user"

GLOBAL_APPLICATION_ID= "schedulertest"
GLOBAL_CALLBACK_URL="http://localhost:8900/scheduler/v1/loopbacktest/vid"

CMSO_STARTUP_WAIT_TIME="600s"



cmFailurePath= "robot/assets/templates/FailureCasesChangeManagement"
GLOBAL_CM_FAILURE_TEMPLATES= [f for f in listdir(cmFailurePath) if isfile(join(cmFailurePath, f))]

OneVNFImmediateFailurePath="robot/assets/templates/OneVNFImmediateFailureCases"
GLOBAL_CM_ONEVNF_FAILURE_TEMPLATES=[f for f in listdir(OneVNFImmediateFailurePath) if isfile(join(OneVNFImmediateFailurePath, f))]

MultipleVNFImmediateFailurePath="robot/assets/templates/MutipleVNFImmediateFailureCases"
GLOBAL_CM_MULTIPLE_VNF_FAILURE_TEMPLATES=[f for f in listdir(MultipleVNFImmediateFailurePath) if isfile(join(MultipleVNFImmediateFailurePath, f))]


DELETE_TICKET_ENVS = [
    {"scheduler" : "dummy", "vtm" : "dummy"},
    {"scheduler" : "dummy", "vtm" : "dummy"},
]

NODES = "node1,node2,node3,node4";
 