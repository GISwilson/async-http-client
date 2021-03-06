/*
 * Copyright (c) 2016 AsyncHttpClient Project. All rights reserved.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */
package org.asynchttpclient.example.completable;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import java.net.InetSocketAddress;
import java.util.List;
import javax.net.ssl.SSLSession;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.Response;

import java.io.IOException;
import org.asynchttpclient.netty.request.NettyRequest;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class CompletableFutures {
  public static void main(String[] args) throws IOException {
    try (AsyncHttpClient asyncHttpClient = asyncHttpClient()) {
      asyncHttpClient
              .prepareGet("http://www.example.com/")
              .execute(new AsyncCompletionHandler<Response>(){

                @Override
                public State onStatusReceived(HttpResponseStatus responseStatus) throws Exception {
                  System.out.println("onStatusReceived");
                  return super.onStatusReceived(responseStatus);
                }

                @Override
                public State onHeadersReceived(HttpHeaders headers) throws Exception {
                  System.out.println("onStatusReceived");
                  return super.onHeadersReceived(headers);
                }

                @Override
                public State onBodyPartReceived(HttpResponseBodyPart bodyPart) throws Exception {
                  System.out.println("onStatusReceived");
                  return super.onBodyPartReceived(bodyPart);
                }

                @Override
                public State onTrailingHeadersReceived(HttpHeaders headers) throws Exception {
                  System.out.println("onStatusReceived");
                  return super.onTrailingHeadersReceived(headers);
                }

                @Override
                public void onThrowable(Throwable t) {
                  System.out.println("ERROR:" + t);
                }

                @Override
                public Response onCompleted(Response response) throws Exception {
                  System.out.println("onCompleted");
                  return response;
                }

                @Override
                public void onHostnameResolutionAttempt(String name) {
                  System.out.println("onHostnameResolutionAttempt");
                }

                @Override
                public void onHostnameResolutionSuccess(String name, List<InetSocketAddress> addresses) {
                  System.out.println("onHostnameResolutionSuccess");
                }

                @Override
                public void onHostnameResolutionFailure(String name, Throwable cause) {
                  System.out.println("onHostnameResolutionFailure");

                }

                @Override
                public void onTcpConnectAttempt(InetSocketAddress remoteAddress) {
                  System.out.println("onTcpConnectAttempt");
                  super.onTcpConnectAttempt(remoteAddress);
                }

                @Override
                public void onTcpConnectSuccess(InetSocketAddress remoteAddress, Channel connection) {
                  System.out.println("onTcpConnectSuccess");
                  super.onTcpConnectSuccess(remoteAddress, connection);

                }

                @Override
                public void onTcpConnectFailure(InetSocketAddress remoteAddress, Throwable cause) {
                  System.out.println("onTcpConnectFailure");

                }

                @Override
                public void onTlsHandshakeAttempt() {
                  System.out.println("onTlsHandshakeAttempt");

                }

                @Override
                public void onTlsHandshakeSuccess(SSLSession sslSession) {
                  System.out.println("onTlsHandshakeSuccess");

                }

                @Override
                public void onTlsHandshakeFailure(Throwable cause) {
                  System.out.println("onTlsHandshakeFailure");

                }

                @Override
                public void onConnectionPoolAttempt() {
                  System.out.println("onConnectionPoolAttempt");

                }

                @Override
                public void onConnectionPooled(Channel connection) {
                  System.out.println("onConnectionPooled");

                }

                @Override
                public void onConnectionOffer(Channel connection) {
                  System.out.println("onConnectionOffer");

                }

                @Override
                public void onRequestSend(NettyRequest request) {
                  System.out.println("onRequestSend");

                }

                @Override
                public void onRetry() {
                  System.out.println("onRetry");

                }
              })
              .toCompletableFuture()
              .thenApply(Response::getResponseBody)
              .thenAccept(System.out::println)
              .join();
    }
  }
}
