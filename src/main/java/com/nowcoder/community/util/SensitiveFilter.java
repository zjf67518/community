package com.nowcoder.community.util;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符
    private static final String REPLACEMENT = "***";

    // 根节点初始化
    private TrieNode rootNode = new TrieNode();

    //调用类的构造器之后，此方法会自动被调用
    @PostConstruct
    public void init() {
        // 类路径/target/classes（编译后所有文件都会进入classes之下）
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                // 字节流转换为字符流 new InputStreamReader() ； 转换为缓冲流读取效率更高
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
                String keyword;
                while((keyword = reader.readLine()) != null) {
                    // 添加到前缀树去
                    this.addKeyword(keyword);
                }
        } catch(IOException e) {
            logger.error("加载敏感词失败" + e.getMessage());
        }

    }

    // 将一个敏感词添加到前缀树中去
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for(int i=0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if(subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            // 指向子节点，进入下一轮循环
            tempNode = subNode;
            // 设置结束标识
            if(i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if(StringUtils.isBlank(text)) {
            return null;
        }
        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while(begin < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if(isSymbol(c)) {
                // 若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 如果最后一位仍然是符号，那就比较下一个，不能直接加，否则会越界
                if(position == text.length()-1){
                    begin++;
                    position = begin;
                    continue;
                }
                // 无论符号在开头或中间，指针3都向下走一步
                position++;
                continue;
            }
            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null) {
                // 以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if(tempNode.isKeywordEnd()) {
                // 发现了敏感词，将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                if(position < text.length() - 1) {
                    position++;
                }
            }
        }
        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 前缀树
    private class TrieNode {

        // 关键词结束的标识
        private boolean isKeywordEnd = false;

        // 子节点(key是下级字符，value是下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

}
