package com.xcoder.dueros.baidu;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.baidu.dueros.bot.BaseBot;
import com.baidu.dueros.certificate.Certificate;
import com.baidu.dueros.data.request.IntentRequest;
import com.baidu.dueros.data.request.LaunchRequest;
import com.baidu.dueros.data.request.SessionEndedRequest;
import com.baidu.dueros.data.response.OutputSpeech;
import com.baidu.dueros.data.response.OutputSpeech.SpeechType;
import com.baidu.dueros.data.response.Reprompt;
import com.baidu.dueros.data.response.card.TextCard;
import com.baidu.dueros.model.Response;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyTaxBot extends BaseBot {

    public MyTaxBot(Certificate certificate) throws IOException {
        super(certificate);
    }

    public MyTaxBot(HttpServletRequest request) throws IOException {
        super(request);
    }

    public MyTaxBot(String request) throws IOException {
        super(request);
    }

    protected Response onLaunch(LaunchRequest launchRequest) {
        TextCard textCard = new TextCard("所得税为您服务");
        textCard.setUrl("www:....");
        textCard.setAnchorText("setAnchorText");
        textCard.addCueWord("欢迎进入");
        OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "所得税为您服务");
        Response response = new Response(outputSpeech, textCard);
        return response;
    }

    protected Response onInent(IntentRequest intentRequest) {
        if ("inquiry".equals(intentRequest.getIntentName())) {
            if (this.getSlot("monthlysalary") == null) {
                this.ask("monthlysalary");
                return this.askSalary();
            } else if (this.getSlot("location") == null) {
                this.ask("location");
                return this.askLocation();
            } else if (this.getSlot("compute_type") == null) {
                this.ask("compute_type");
                return this.askComputeType();
            } else {
                return this.compute();
            }
        } else {
            return null;
        }
    }

    protected Response onSessionEnded(SessionEndedRequest sessionEndedRequest) {
        TextCard textCard = new TextCard("感谢使用所得税服务");
        textCard.setAnchorText("setAnchorText");
        textCard.addCueWord("欢迎再次使用");
        OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "欢迎再次使用所得税服务");
        Response response = new Response(outputSpeech, textCard);
        return response;
    }

    private Response askLocation() {
        TextCard textCard = new TextCard("您所在的城市是哪里呢?");
        textCard.setUrl("www:......");
        textCard.setAnchorText("setAnchorText");
        textCard.addCueWord("您所在的城市是哪里呢?");
        this.setSessionAttribute("key_1", "value_1");
        this.setSessionAttribute("key_2", "value_2");
        OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "您所在的城市是哪里呢?");
        Reprompt reprompt = new Reprompt(outputSpeech);
        Response response = new Response(outputSpeech, textCard, reprompt);
        return response;
    }

    private Response askSalary() {
        TextCard textCard = new TextCard("您的税前工资是多少呢?");
        textCard.setUrl("www:......");
        textCard.setAnchorText("链接文本");
        textCard.addCueWord("您的税前工资是多少呢?");
        this.setSessionAttribute("key_1", "value_1");
        this.setSessionAttribute("key_2", "value_2");
        OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "您的税前工资是多少呢?");
        Reprompt reprompt = new Reprompt(outputSpeech);
        Response response = new Response(outputSpeech, textCard, reprompt);
        return response;
    }

    private Response askComputeType() {
        TextCard textCard = new TextCard("请选择您要查询的种类");
        textCard.setAnchorText("setAnchorText");
        textCard.addCueWord("请选择您要查询的种类");
        this.setSessionAttribute("key_1", "value_1");
        this.setSessionAttribute("key_2", "value_2");
        OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, "请选择您要查询的种类");
        Reprompt reprompt = new Reprompt(outputSpeech);
        Response response = new Response(outputSpeech, textCard, reprompt);
        return response;
    }

    private Response compute() {
        String monthlysalary = this.getSlot("monthlysalary");
        String location = this.getSlot("location");
        String type = this.getSlot("compute_type");
        String ret = "月薪" + monthlysalary + "城市" + location + "个税种类" + type;
        TextCard textCard = new TextCard(ret);
        textCard.setAnchorText("setAnchorText");
        textCard.addCueWord("查询成功");
        this.setSessionAttribute("key_1", "value_1");
        this.setSessionAttribute("key_2", "value_2");
        OutputSpeech outputSpeech = new OutputSpeech(SpeechType.PlainText, ret);
        Reprompt reprompt = new Reprompt(outputSpeech);
        this.endDialog();
        Response response = new Response(outputSpeech, textCard, reprompt);
        return response;
    }
}
