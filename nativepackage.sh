#!/bin/sh

scala-cli package --native-image -f *.scala -o ziohttp.bin -- --no-fallback --enable-url-protocols=http,https -Djdk.http.auth.tunneling.disabledSchemes= --install-exit-handlers --enable-http --initialize-at-run-time=io.netty.channel.DefaultFileRegion --initialize-at-run-time=io.netty.channel.epoll.Native --initialize-at-run-time=io.netty.channel.epoll.Epoll --initialize-at-run-time=io.netty.channel.epoll.EpollEventLoop --initialize-at-run-time=io.netty.channel.epoll.EpollEventArray --initialize-at-run-time=io.netty.channel.kqueue.KQueue --initialize-at-run-time=io.netty.channel.kqueue.KQueueEventLoop --initialize-at-run-time=io.netty.channel.kqueue.KQueueEventArray --initialize-at-run-time=io.netty.channel.kqueue.Native --initialize-at-run-time=io.netty.channel.unix.Limits --initialize-at-run-time=io.netty.channel.unix.Errors --initialize-at-run-time=io.netty.channel.unix.IovArray --initialize-at-run-time=io.netty.handler.codec.compression.ZstdOptions --initialize-at-run-time=io.netty.incubator.channel.uring.IOUringEventLoopGroup --initialize-at-run-time=io.netty.incubator.channel.uring.Native --initialize-at-run-time=io.netty.handler.ssl.BouncyCastleAlpnSslUtils