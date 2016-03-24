package com.andrewkingmarshall;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaMainServer {

    public static void main(String[] args) {
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        TextLineCodecFactory factory = new TextLineCodecFactory(Charset.forName("UTF-8"));
        factory.setDecoderMaxLineLength(4000);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));
        acceptor.getSessionConfig().setReadBufferSize(4000);
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
        acceptor.setHandler(new QueueServerHandler());
        try {
            acceptor.bind(new InetSocketAddress(QueueServerHandler.PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
