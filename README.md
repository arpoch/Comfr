# Comfr

## Idea

* This project is based on the idea of peer-peer communication and centralized storage.The Users can communicate and transfer Data cross the network without any need of centralized authority also giving the ability to the user to store data in a centralized location, accessible to others.

* Another important issue taken in account is portability and ease of installation. The client side will be developed as a web extension thus making it protable across devices without the need of additional software installation.


## Implementation

1. **Client Side**

   * [*WebRTC*](https://tools.ietf.org/html/rfc7478) is used for establishing peer-peer connection between clients. [*WebSocket Protocol*](https://tools.ietf.org/html/rfc6455) is used for signalling purpose and sending files to and from the server for storage.
   * The client side will be installed as an extension on the browser thus making it compatible with different desktop browsers such as Chrome, Firefox.
2. **Server Side**

   * For now an Android app is used to fulfil the purpose of a server using [*WebSocket Protocol*](https://tools.ietf.org/html/rfc6455) and as a storage location.
   * The server side for LAN will be implemented using mobile app for storage and signalling purpose. For WAN cloud storage will be used.

## Scope

* For now the scope of the project is limited to LAN thus user can send messages and files on the same LAN.
* For now the server side is limited to Android.
*For client side any browser that supports WebSocket protocol,WebRTC framework will be supported

## Installation

1. **Server Side**

   * The server side code is given in *WebSocket_Server* directory.
   * Clone the repo and open the *WebSocket_Server* with Android Studio
2. **Client Side**

   * The client side code is given in *Client* directory.
   * The code is implemented using WebSocket protocol,WebRTC framework thus any browser which supports these frameworks can be used.

## What's Next

Since the WebRTC provides real-time communication capabilities to the application thus different use cases can be made such as screen-sharing, video-calling, chat-messages and much more.
