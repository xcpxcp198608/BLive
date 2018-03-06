package com.wiatec.blive.pojo

/**
 * the result that return to getUserInfo after getUserInfo's request
 */
class ResultInfo<T> {

    /**
     * status code
     */
    var code: Int = 0

    /**
     * custom message
     */
    var message: String? = null

    var data: T? = null

    /**
     * data
     */
    var dataList: List<T>? = null

    override fun toString(): String {
        return "ResultInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", dataList=" + dataList +
                '}'
    }

    companion object {

        /**
         * request success (GET method)
         */
        val CODE_OK = 200

        /**
         * create or update success (POST(insert) / PUT (update) method)
         */
        val CODE_CREATED = 201

        /**
         * delete data success (DELETE method)
         */
        val CODE_DELETED = 204

        /**
         * request operate failure (GET , POST , PUT , DELETE method)
         */
        val CODE_INVALID = 400

        /**
         * getUserInfo permission error (UserName , Password , Token error)
         */
        val CODE_UNAUTHORIZED = 401

        /**
         * request resource not exists (GET)
         */
        val CODE_NO_FOUND = 404

        /**
         * server error
         */
        val CODE_SERVER_ERROR = 500

        /**
         * request success (GET method)
         */
        val STATUS_OK = "Request Success"

        /**
         * create or update success (POST(insert) / PUT (update) method)
         */
        val STATUS_CREATED = "Created Success"

        /**
         * delete data success (DELETE method)
         */
        val STATUS_DELETED = "Deleted Success"

        /**
         * request operate failure (GET , POST , PUT , DELETE method)
         */
        val STATUS_INVALID = "Request Invalid"

        /**
         * getUserInfo permission error (UserName , Password , Token error)
         */
        val STATUS_UNAUTHORIZED = "Authorization Error"

        /**
         * request resource not exists (GET)
         */
        val STATUS_NO_FOUND = "Resource no found"

        /**
         * server error
         */
        val STATUS_SERVER_ERROR = "Server error, please try again later"
    }
}
