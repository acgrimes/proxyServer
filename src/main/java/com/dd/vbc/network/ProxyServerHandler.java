package com.dd.vbc.network;

import com.dd.vbc.enums.Request;
import com.dd.vbc.enums.Response;
import com.dd.vbc.service.BallotRequestService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Listing 2.1 EchoServerHandler
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
@Sharable
public class ProxyServerHandler extends ChannelInboundHandlerAdapter {

    private ElectionResponse electionResponse;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf in = (ByteBuf) msg;

        byte[] requestBytes = ByteBufUtil.getBytes(in);

        ElectionRequest electionRequest = new ElectionRequest();
        electionRequest.deserialize(requestBytes);
        Request request = electionRequest.getRequest();
        System.out.println("Client request received with request type: "+request.name());
        switch(request) {
            case Login: {
                System.out.println("Server received: " + electionRequest.getLoginCredentials().toString());
                electionResponse = new ElectionResponse();
                electionResponse.setResponse(Response.Authentication);
                break;
            }
            case BallotRequest: {
                System.out.println("Server received Voter Id: "+electionRequest.getVoter().getId().toString());
                BallotRequestService ballotRequestService = new BallotRequestService();
                electionResponse = ballotRequestService.getBallotResponse(electionRequest.getVoter());
                break;
            }
        }
        System.out.println("Sending response type: "+electionResponse.getResponse().name());
        ByteBuf byteBuf = Unpooled.copiedBuffer(electionResponse.serialize());
        System.out.println("Response ByteBuf capacity: "+byteBuf.capacity());
        ctx.writeAndFlush(Unpooled.copiedBuffer(byteBuf));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
