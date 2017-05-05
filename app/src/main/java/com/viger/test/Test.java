package com.viger.test;

import java.util.List;

/**
 * Created by Administrator on 2017/4/20.
 */

public class Test {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * lottery : ssc
         * playOneList : [{"playOneId":"rx2","playOneName":"任选二","playTwoList":[{"playTwoId":"rx2_zx","playTwoName":"任二直选"}]}]
         */

        private String lottery;
        private List<PlayOneListBean> playOneList;

        public String getLottery() {
            return lottery;
        }

        public void setLottery(String lottery) {
            this.lottery = lottery;
        }

        public List<PlayOneListBean> getPlayOneList() {
            return playOneList;
        }

        public void setPlayOneList(List<PlayOneListBean> playOneList) {
            this.playOneList = playOneList;
        }

        public static class PlayOneListBean {
            /**
             * playOneId : rx2
             * playOneName : 任选二
             * playTwoList : [{"playTwoId":"rx2_zx","playTwoName":"任二直选"}]
             */

            private String playOneId;
            private String playOneName;
            private List<PlayTwoListBean> playTwoList;

            public String getPlayOneId() {
                return playOneId;
            }

            public void setPlayOneId(String playOneId) {
                this.playOneId = playOneId;
            }

            public String getPlayOneName() {
                return playOneName;
            }

            public void setPlayOneName(String playOneName) {
                this.playOneName = playOneName;
            }

            public List<PlayTwoListBean> getPlayTwoList() {
                return playTwoList;
            }

            public void setPlayTwoList(List<PlayTwoListBean> playTwoList) {
                this.playTwoList = playTwoList;
            }

            public static class PlayTwoListBean {
                /**
                 * playTwoId : rx2_zx
                 * playTwoName : 任二直选
                 */

                private String playTwoId;
                private String playTwoName;

                public String getPlayTwoId() {
                    return playTwoId;
                }

                public void setPlayTwoId(String playTwoId) {
                    this.playTwoId = playTwoId;
                }

                public String getPlayTwoName() {
                    return playTwoName;
                }

                public void setPlayTwoName(String playTwoName) {
                    this.playTwoName = playTwoName;
                }
            }
        }
    }
}
