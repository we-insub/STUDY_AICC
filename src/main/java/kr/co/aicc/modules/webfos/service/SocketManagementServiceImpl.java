/*
 * AICC (AI Caption Center) version 1.0
 *
 *  Copyright ⓒ 2020 sorizava corp. All rights reserved.
 *
 *  This is a proprietary software of sorizava corp, and you may not use this file except in
 *  compliance with license agreement with sorizava corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of sorizava corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 *
 * @package kr.co.aicc.infra.config
 * @file SecurityConfig.java
 * @author developer
 * @since 2020. 06. 08
 * @version 1.0
 * @title AICC
 * @description
 *
 *
 * << 개정이력 (Modification Information) >>
 *
 *   수정일		수정자	수정내용
 * -------------------------------------------------------------------------------
 * 2020. 06. 18	developer		 최초생성
 * -------------------------------------------------------------------------------
 */
package kr.co.aicc.modules.webfos.service;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SocketManagementServiceImpl implements SocketManagementService {

	private Map<Integer, Object> socketInfo = new HashMap<Integer, Object>();
	
	@Override
	public int createConnection(String transIp, String transPort) {
		Socket socket = new Socket();
		Integer tranLocalPort = null;
		
		try {
			socket.connect(new InetSocketAddress(transIp, Integer.parseInt(transPort)));
			
			log.debug("connection : {},({})", socket, socket.getLocalPort());
			
			tranLocalPort = socket.getLocalPort();
			
			socketInfo.put(tranLocalPort, socket);
		} catch (UnknownHostException e) {
			log.error("UnknownHostException....: {}, socketInfo : {}", e.toString(), socketInfo.isEmpty());
		} catch (IOException e) {
			log.error("IOException....: {}, socketInfo : {}", e.toString(), socketInfo.isEmpty());
		}
		
		return ObjectUtils.isEmpty(tranLocalPort) ? 0 : tranLocalPort;
	}
	
	@Override
	public void transmissionMessage(int localPort, String message) {
		if (!ObjectUtils.isEmpty(socketInfo.get(localPort))) {
			Socket targetSocket = (Socket)socketInfo.get(localPort);
			log.debug("transmission : {},({})", targetSocket, targetSocket.getLocalPort());
			
			try {
				Writer writer = new OutputStreamWriter(targetSocket.getOutputStream(), "UTF-8");
//				writer.write("connect");
//				writer.flush();
//				
//				byte[] bytes = new byte[1000];
//				int readByteCount = targetSocket.getInputStream().read(bytes);
//
//				String res = new String(bytes, 0, readByteCount, "UTF-8");
//
//				log.info("[연결 상태 체크]: {}", res);
				
				writer.write(message);
				writer.flush();
				
			} catch (IOException e) {
				log.error("IOException....: {}, socketInfo : {}", e.toString(), socketInfo.isEmpty());
			}
		}
	}
	
	@Override
	public void removeTransSocket(int localPort) {
		if (!ObjectUtils.isEmpty(socketInfo.get(localPort))) {
			try {
				log.debug("removeTransSocket : {}", localPort);
				Socket targetSocket = (Socket)socketInfo.get(localPort);
				targetSocket.close();
			} catch (IOException e) {
				log.error("targetSocket.close() : {}", e.toString());
			}
			
			socketInfo.remove(localPort);
		}
	}
	
	@Override
	public int createConnectionUDP(String transIp, String transPort) {
        Integer tranLocalPort = null;
        
		try {
			DatagramSocket datagramSocket = new DatagramSocket();		
			datagramSocket.connect(new InetSocketAddress(transIp, Integer.parseInt(transPort)));
			
			log.debug("connection : {},({})", datagramSocket, datagramSocket.getLocalPort());
			
			tranLocalPort = datagramSocket.getLocalPort();
			
			socketInfo.put(tranLocalPort, datagramSocket);
	
		} catch (IOException e) {
			log.error("IOException....: {}, socketInfo : {}", e.toString(), socketInfo.isEmpty());
		}
		
		return ObjectUtils.isEmpty(tranLocalPort) ? 0 : tranLocalPort;
	}
	
	@Override
	public void transmissionMessageUDP(List<Integer> localPortList, String message) {
		if (!CollectionUtils.isEmpty(localPortList)) {
			for (int localPort : localPortList) {
				if (!ObjectUtils.isEmpty(socketInfo.get(localPort))) {
					DatagramSocket targetDatagramSocket = (DatagramSocket)socketInfo.get(localPort);
					
					try {
						log.info("message : {}", message);
						//byte buffer[] = message.getBytes("utf-8");
			    		//DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
						String filterMessage = filterChar(message);
						log.info("filterMessage : {}", filterMessage);
						byte buffer[] = filterMessage.getBytes("euc-kr");
						DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
						
						targetDatagramSocket.send(dp);
					} catch (Exception e) {
						log.error("Exception....: {}, socketInfo : {}", e.toString(), socketInfo.isEmpty());
					}
				}
			}
			
		}
	}
	
	@Override
	public void removeTransSocketUDP(int localPort) {
		if (!ObjectUtils.isEmpty(socketInfo.get(localPort))) {
			try {
				log.debug("removeTransSocket : {}", localPort);
				DatagramSocket targetDatagramSocket = (DatagramSocket)socketInfo.get(localPort);
				targetDatagramSocket.close();
			} catch (Exception e) {
				log.error("targetSocket.close() : {}", e.toString());
			}
			
			socketInfo.remove(localPort);
		}
	}
	
	private String filterChar(String str) { 
        String space = " ";
        char[] spaceArray = space.toCharArray();
        
        char[] charArray = str.toCharArray();
        String change = "";
                    
        for (int j = 0; j < charArray.length; j++) {
        	byte value = (byte)charArray[j];
        	log.debug("value[{}] : {}", j, value);
        	if (j != 0 && value == -96) {
        		byte val = (byte)charArray[j-1];
        		if (!((val >= 0) || (val < 0))) {
        			charArray[j] = spaceArray[0];
        		}
        	}
            change+= Character.toString(charArray[j]);
        }
        
        return change;
	}
	
	private String replaceWords(String words) {
		String result = null;
		
		result = words.replaceAll("&nbsp;", "");
		
		return result;
	}
}
