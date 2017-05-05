package com.viger.netease.bean;

/**
 * Created by Administrator on 2017/5/3.
 */

public class SpecialItem {


    /**
     * digest :
     * docid : CJBE5BVL00237VUV
     * imgsrc : http://cms-bucket.nosdn.127.net/b3590273fd4447bda1469cc63b7c5d5f20170501092714.jpeg
     * imgsum : 0
     * ipadcomment :
     * lmodify : 2017-05-01 09:31:04
     * ltitle : “平语”近人——习近平的“劳动观”
     * postid : CJBE5BVL00237VUV
     * ptime : 2017-05-01 09:25:10
     * replyCount : 10
     * source : 新华网
     * tag :
     * title : “平语”近人——习近平的“劳动观”
     * url : http://3g.163.com/ntes/17/0501/09/CJBE5BVL00237VUV.html
     * votecount : 0
     */

    private String digest;
    private String docid;
    private String imgsrc;
    private int imgsum;
    private String ipadcomment;
    private String lmodify;
    private String ltitle;
    private String postid;
    private String ptime;
    private int replyCount;
    private String source;
    private String tag;
    private String title;
    private String url;
    private int votecount;

    private boolean isTitle;
    private String title_name;
    private String index;

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public int getImgsum() {
        return imgsum;
    }

    public void setImgsum(int imgsum) {
        this.imgsum = imgsum;
    }

    public String getIpadcomment() {
        return ipadcomment;
    }

    public void setIpadcomment(String ipadcomment) {
        this.ipadcomment = ipadcomment;
    }

    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
    }

    public String getLtitle() {
        return ltitle;
    }

    public void setLtitle(String ltitle) {
        this.ltitle = ltitle;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVotecount() {
        return votecount;
    }

    public void setVotecount(int votecount) {
        this.votecount = votecount;
    }
}
