package net.alexramos.dynamodb.util;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class SqlUtil {
    
    public static Spliterator<String> splitStatements(String sql) {
        return new Spliterators.AbstractSpliterator<String>(Long.MAX_VALUE, 0) {
            int start = 0;
            int next = 0;
            @Override
            public boolean tryAdvance(Consumer<? super String> action) {
                if(start >= sql.length()) {
                    return false;
                }
                while(Character.isWhitespace(sql.charAt(next))) {
                    next++;
                }
                while(sql.charAt(next) != ';') {
                    ++next;
                    if(next < sql.length() - 1) {
                        if(sql.charAt(next) == '-' && sql.charAt(next+1) == '-') {
                            while(sql.charAt(next) != '\n')
                                ++next;
                        }
                        else if(sql.charAt(next) == '/' && sql.charAt(next+1) == '*') {
                            while(sql.charAt(next) != '*' || sql.charAt(next+1) != '/') {
                                ++next;
                            }
                        }
                    }
                }
                action.accept(sql.substring(start, next));               
                start = ++next;
                return true;
            }
        };
    }

}
