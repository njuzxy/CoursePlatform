package com.zxyu.test.Helper;

public enum StateEnum {
    NotSubmitted, Submitted, Scored,//submit
    Published, End, Finished,//assignment : published已发布, end已截止, finished已运行, scored已出分
    Waiting,//submit_score
    Unread,Read,//notice
}
