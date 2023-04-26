package com.tidal.stream.filetransfer;

import com.jcraft.jsch.*;
import com.tidal.stream.filehandler.FileOutWriter;
import com.tidal.stream.filehandler.Finder;
import org.apache.log4j.Logger;

import java.nio.file.Paths;



public class PublicSFTPConnection {
//
//    Logger logger = Logger.getLogger(PublicSFTPConnection.class);
//
//    private final String host;
//    private final String sftpUserName;
//    private final int portNumber;
//    private final String preferredAuthentications;
//    private final String strictHostKeyChecking;
//    private final String sftpAuthKey;
//
//    private PublicSFTPConnection(Connector connector) {
//        this.host = connector.host;
//        this.sftpUserName = connector.sftpUserName;
//        this.portNumber = connector.portNumber;
//        this.preferredAuthentications = connector.preferredAuthentications;
//        this.strictHostKeyChecking = connector.strictHostKeyChecking;
//        this.sftpAuthKey = connector.sftpAuthKey;
//    }
//
//    private Session session = null;
//
//    private void createConnection() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Creating new SFTP connection with:").append(System.lineSeparator());
//        sb.append("Host: ").append(host).append(System.lineSeparator());
//        sb.append("SFTP User: ").append(sftpUserName).append(System.lineSeparator());
//        sb.append("Port Number: ").append(portNumber).append(System.lineSeparator());
//        sb.append("StrictHostKeyChecking: ").append(strictHostKeyChecking);
//
//        logger.info(sb.toString());
//
//        try {
//            JSch jSch = new JSch();
//            //sftpAuthKey is always the path to the key file, not the key value itself
//            jSch.addIdentity(sftpAuthKey);
//            session = jSch.getSession(sftpUserName, host, portNumber);
//            session.setConfig("PreferredAuthentications", preferredAuthentications);
//            java.util.Properties config = new java.util.Properties();
//            config.put("StrictHostKeyChecking", strictHostKeyChecking);
//            session.setConfig(config);
//            session.connect();
//        } catch (JSchException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    private ChannelSftp channelSftp;
//
//    public void uploadFileToRemote(String localFilePath, String remoteFilePath) {
//        try {
//            Channel sftp;
//            sftp = session.openChannel("sftp");
//            sftp.connect();
//            channelSftp = (ChannelSftp) sftp;
//            channelSftp.put(localFilePath, remoteFilePath);
//        } catch (JSchException | SftpException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    /**
//     * Method terminates the SFTP connection. </br>
//     * It would delete the key file temporarily created for authentication.
//     */
//
//    public void terminateConnection() {
//        session.disconnect();
//        channelSftp.disconnect();
//        channelSftp.exit();
//        FileOutWriter.deleteTargetFolderDirectory(Paths.get("keys", "SFTP_Dev.pem").toString());
//    }
//
//    /**
//     * The Connector class is actually a static 'Builder' class aptly named for creating a 'connection' to an SFTP server</br>
//     * The private variables can be overridden by the class methods if a need arise.
//     */
//
//
//    public static class Connector {
//
//        //The default value is given assuming the organization has only one SFTP URL. This needs to be changed if there are multiple values
//       //todo - ADD THE SFTP URL
//        private String host = getProperty("sftpUrl") == null ? "{{{ADD THE SFTP URL HERE}}}" : getProperty("sftpUrl");
//        private String sftpUserName = getProperty("sftp.user.name");
//        //The below default value can be overridden using the corresponding method
//        private int portNumber = getProperty("sftp.port.number") == null ? 22 : Integer.parseInt(getProperty("sftp.port.number"));
//        //The below default value can be overridden using the corresponding method
//        private String preferredAuthentications = getProperty("sftp.preferred.auth") == null ? "publickey,keyboard-interactive,password" : getProperty("sftp.preferred.auth");
//        //The below default value can be overridden using the corresponding method
//        private String strictHostKeyChecking = "no";
//        private String sftpAuthKey = getProperty("sftp.auth.key") == null ? "Invalid Private Key" : getProperty("sftp.auth.key");
//
//        /**
//         * The host url to connect. Omit the prefix 'sftp://' while adding this value
//         * @param host The sftp host to connect
//         * @return A self reference
//         */
//        public Connector host(String host) {
//            this.host = host;
//            return this;
//        }
//
//
//        public Connector sftpUser(String sftpUserName) {
//            this.sftpUserName = sftpUserName;
//            return this;
//        }
//
//        public Connector portNumber(int portNumber) {
//            this.portNumber = portNumber;
//            return this;
//        }
//
//        public Connector preferredAuthentications(String preferredAuthentications) {
//            this.preferredAuthentications = preferredAuthentications;
//            return this;
//        }
//
//        public Connector strictHostKeyChecking(String strictHostKeyChecking) {
//            this.strictHostKeyChecking = strictHostKeyChecking;
//            return this;
//        }
//
//
//        public PublicSFTPConnection connect() {
//            //The encrypted authentication key will be missing the type information.
//            //The createAuthKey method will transform it with the attached type information
//            sftpAuthKey = createAuthKey(sftpAuthKey);
//
//            PublicSFTPConnection publicSFTPConnection = new PublicSFTPConnection(this);
//            publicSFTPConnection.createConnection();
//            return publicSFTPConnection;
//        }
//    }
//
//    private static String createAuthKey(String sftpAuthKey) {
//        String folderName = "keys";
//        FileOutWriter.createTargetFolderDirectory(folderName);
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("-----BEGIN RSA PRIVATE KEY-----").append(System.lineSeparator());
//        sb.append(sftpAuthKey).append(System.lineSeparator());
//        sb.append("-----END RSA PRIVATE KEY-----");
//
//        Finder.setTargetAsBaseFolder();
//        FileOutWriter.writeFileToTargetFolder(sb.toString(), Paths.get(folderName, "SFTP_Dev.pem").toString());
//        String keyFilePath = Finder.findFilePath("SFTP_Dev.pem");
//        Finder.resetToResourceFolder();
//        return keyFilePath;
//    }



}


