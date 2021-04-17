package be.alexandre01.dreamzon.network.client.communication;

import be.alexandre01.dreamzon.network.utils.message.Message;

public class ResponseData {
    private int intValue;
    private String stringValue;
    private Message messageValue;

    public Message getMessageValue() {
        System.out.println("requestMessageValue");
        return messageValue;
    }

    public void setMessageValue(Message messageValue) {
        this.messageValue = messageValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

}
