package com.app.talentum.cubelizer.cubelizer.persistencia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Astrid on 29/11/2016.
 */

public class JsonRespon {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("result")
        @Expose
        private Result result;

        /**
         *
         * @return
         * The status
         */
        public String getStatus() {
            return status;
        }

        /**
         *
         * @param status
         * The status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         *
         * @return
         * The message
         */
        public String getMessage() {
            return message;
        }

        /**
         *
         * @param message
         * The message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         *
         * @return
         * The result
         */
        public Result getResult() {
            return result;
        }

        /**
         *
         * @param result
         * The result
         */
        public void setResult(Result result) {
            this.result = result;
        }

    @Override
    public String toString() {
        return "JsonRespon{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}

