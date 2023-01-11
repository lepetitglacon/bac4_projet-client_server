package common

import java.io.Serializable

class Request(val type: RequestType, val data: Any?) : Serializable {
}