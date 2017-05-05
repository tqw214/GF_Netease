package com.viger.netease.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class AdsBean implements Serializable{

    private int next_req;
    private List<AdsDetail> ads;

    public int getNext_req() {
        return next_req;
    }

    public void setNext_req(int next_req) {
        this.next_req = next_req;
    }

    public List<AdsDetail> getAds() {
        return ads;
    }

    public void setAds(List<AdsDetail> ads) {
        this.ads = ads;
    }

    public class AdsDetail implements Serializable{

        private String action;
        private Action action_params;
        private List<String> res_url;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Action getAction_params() {
            return action_params;
        }

        public void setAction_params(Action action_params) {
            this.action_params = action_params;
        }

        public List<String> getRes_url() {
            return res_url;
        }

        public void setRes_url(List<String> res_url) {
            this.res_url = res_url;
        }

        public class Action implements Serializable{

            private String link_url;

            public String getLink_url() {
                return link_url;
            }

            public void setLink_url(String link_url) {
                this.link_url = link_url;
            }
        }
    }
}
