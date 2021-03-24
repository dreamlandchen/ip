package com.dreamland.iplocation.util;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * @Author zora
 * @Date 17:44 2021/03/23
 * @Description:
 * @Modified By
 */
public class IpUtil {
    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

    private static String DB_FILE_NAME = "ip2region/ip2region.db";

    private static int IPV4_BYTE = 4;

    private static String FILE_SUFFIX_JAR = ".JAR";

    public static String getCityInfo(String ip) throws Exception{


        String dbPath = getDbPath();
        DbConfig config = new DbConfig();
        DbSearcher searcher = new DbSearcher(config, dbPath);
        Method method = searcher.getClass().getMethod("btreeSearch", String.class);
        return ((DataBlock) method.invoke(searcher, ip)).getRegion();
    }

    /**
     * 获取ip2region.db的目录
     * @return
     * @throws IOException
     */
    private static String getDbPath() throws IOException {
        URL dbURL = IpUtil.class.getClassLoader().getResource(DB_FILE_NAME);
        String dbPath;
        if (dbURL == null) {
            String projectPath = getProjectPath(IpUtil.class);
            dbPath = projectPath + "/" + DB_FILE_NAME;
            logger.info("dbPath projectPath is: {} ", dbPath);
        } else {
            dbPath = dbURL.getPath();
            logger.info("dbURL is: {} ", dbPath);
        }
        File file = new File(dbPath);
        if(!file.exists()){
            String tmpDir = System.getProperties().getProperty("java.io.tmpdir");
            dbPath = tmpDir + "ip2region.db";
            logger.info("临时ip2region.db的目录：{}", dbPath);
            file = new File(dbPath);
            FileUtils.copyInputStreamToFile(IpUtil.class.getClassLoader().getResourceAsStream("classpath:ip2region/ip2region.db"), file);
        }
        return dbPath;
    }

    private static String getProjectPath(Class tClass) {
        URL url = tClass.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(FILE_SUFFIX_JAR)) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
            File file = new File(filePath);
            filePath = file.getAbsolutePath();
        }
        return filePath;
    }

    /**
     * 是否是有效的ip
     * @param ip
     * @return
     */
    public static boolean validIP(String ip) {
        if(ip == null || "".equals(ip)){
            return false;
        }
        String[] p = ip.split("\\.");
        if (p.length != IPV4_BYTE) {
            return false;
        }
        for(int i = 0; i < p.length; ++i) {
            int val = Integer.valueOf(p[i]);
            if (val > 255) {
                return false;
            }
        }
        return true;
    }

}

