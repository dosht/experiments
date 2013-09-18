from tornado import web, ioloop
from sockjs.tornado import SockJSRouter, SockJSConnection

class Echo(SockJSConnection):
    def on_message(self, msg):
        print "received: %s" % msg
        self.send(msg)

if __name__ == '__main__':
    EchoRouter = SockJSRouter(Echo, '/echo', {'disabled_transports':['websocket']})

    app = web.Application(EchoRouter.urls)
    app.listen(9999)
    ioloop.IOLoop.instance().start()
