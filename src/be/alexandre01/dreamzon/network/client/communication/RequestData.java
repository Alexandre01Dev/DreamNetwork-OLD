package be.alexandre01.dreamzon.network.client.communication;

import be.alexandre01.dreamzon.network.utils.message.Message;

public class RequestData {
    private int intValue;
    private Message messageValue;

    public Message getMessageValue(){
        return messageValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public void setMessageValue(Message messageValue) {
        this.messageValue = messageValue;
    }
}
