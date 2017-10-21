package com.wiatec.blive.pojo;

/**
 * Created by patrick on 21/10/2017.
 * create time : 10:02 AM
 */

public class PushInfo {

    private int error_code;
    private String error_msg;
    private String token;
    private Data data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PushInfo{" +
                "error_code=" + error_code +
                ", error_msg='" + error_msg + '\'' +
                ", token='" + token + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data {
        private String username;
        private String push_url;
        private String push_key;
        private String push_full_url;
        private String play_url;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPush_url() {
            return push_url;
        }

        public void setPush_url(String push_url) {
            this.push_url = push_url;
        }

        public String getPush_key() {
            return push_key;
        }

        public void setPush_key(String push_key) {
            this.push_key = push_key;
        }

        public String getPush_full_url() {
            return push_full_url;
        }

        public void setPush_full_url(String push_full_url) {
            this.push_full_url = push_full_url;
        }

        public String getPlay_url() {
            return play_url;
        }

        public void setPlay_url(String play_url) {
            this.play_url = play_url;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "username='" + username + '\'' +
                    ", push_url='" + push_url + '\'' +
                    ", push_key='" + push_key + '\'' +
                    ", push_full_url='" + push_full_url + '\'' +
                    ", play_url='" + play_url + '\'' +
                    '}';
        }
    }
}
