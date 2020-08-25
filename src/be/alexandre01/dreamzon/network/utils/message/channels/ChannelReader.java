package be.alexandre01.dreamzon.network.utils.message.channels;


import be.alexandre01.dreamzon.network.utils.message.Message;

interface ChannelReader{
    void readChannel(String channel, Message message);
}



