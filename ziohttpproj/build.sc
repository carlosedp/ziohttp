import mill._
import mill.scalalib._
import $ivy.`io.github.alexarchambault.mill::mill-native-image::0.1.22`
import io.github.alexarchambault.millnativeimage.NativeImage

object ziohttp extends ScalaModule with NativeImage {
  def scalaVersion = "3.2.1"
  def nativeImageClassPath = runClasspath()
  def nativeImageName = "ziohttp.bin"
  def nativeImageGraalVmJvmId = T { "graalvm-java17:22.3.0" }
  def nativeImageMainClass = "ZioHttpApp"
  // Options required so ZIO-http can be built by GraalVM native-image
  // Ref. https://github.com/jamesward/hello-zio-http/blob/graalvm/build.sbt#L97-L108
  def nativeImageOptions = Seq(
    "--no-fallback",
    "--enable-url-protocols=http,https",
    "-Djdk.http.auth.tunneling.disabledSchemes=",
    "--install-exit-handlers",
    "--enable-http",
    "--initialize-at-run-time=io.netty.channel.DefaultFileRegion",
    "--initialize-at-run-time=io.netty.channel.epoll.Native",
    "--initialize-at-run-time=io.netty.channel.epoll.Epoll",
    "--initialize-at-run-time=io.netty.channel.epoll.EpollEventLoop",
    "--initialize-at-run-time=io.netty.channel.epoll.EpollEventArray",
    "--initialize-at-run-time=io.netty.channel.kqueue.KQueue",
    "--initialize-at-run-time=io.netty.channel.kqueue.KQueueEventLoop",
    "--initialize-at-run-time=io.netty.channel.kqueue.KQueueEventArray",
    "--initialize-at-run-time=io.netty.channel.kqueue.Native",
    "--initialize-at-run-time=io.netty.channel.unix.Limits",
    "--initialize-at-run-time=io.netty.channel.unix.Errors",
    "--initialize-at-run-time=io.netty.channel.unix.IovArray",
    "--initialize-at-run-time=io.netty.handler.codec.compression.ZstdOptions",
    "--initialize-at-run-time=io.netty.incubator.channel.uring.IOUringEventLoopGroup",
    "--initialize-at-run-time=io.netty.incubator.channel.uring.Native",
    "--initialize-at-run-time=io.netty.handler.ssl.BouncyCastleAlpnSslUtils"
  )

  def ivyDeps = super.ivyDeps() ++ Seq(
    ivy"dev.zio::zio:2.0.4",
    ivy"dev.zio::zio-http:0.0.3",
    ivy"dev.zio::zio-test:2.0.4",
    ivy"dev.zio::zio-test-sbt:2.0.4"
  )

  object test extends Tests {
    def ivyDeps = super.ivyDeps() ++ Seq(
      ivy"dev.zio::zio:2.0.4",
      ivy"dev.zio::zio-http:0.0.3",
      ivy"dev.zio::zio-test:2.0.4",
      ivy"dev.zio::zio-test-sbt:2.0.4"
    )

    def testFramework = "zio.test.sbt.ZTestFramework"

  }
}
