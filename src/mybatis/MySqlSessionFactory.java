package mybatis;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MySqlSessionFactory {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String JDBCURL = "jdbc:mysql://localhost:3306/coding_enhance";
    private static final String USER = "root";
    private static final String PASSWORD = "zht20040723zht";

    @SuppressWarnings("all")
    public <T> T getMapper(Class<T> mapperClass) {
        T o = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperClass}, new MapperInvocationHandler());
        return o;
    }

    static class MapperInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().startsWith("select")) {
                return invokeSelect(method, args);
            }
            return null;
        }

        private Object invokeSelect(Method method, Object[] args) throws Exception {
            Class.forName(DRIVER);
            String sql = createSelectSql(method, args);
            System.out.println(sql);
            try (Connection connection = DriverManager.getConnection(JDBCURL, USER, PASSWORD)) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                if (args != null) {
                    for (int i = 0; i < args.length; i++) {
                        Object arg = args[i];
                        if (arg instanceof Integer) {
                            preparedStatement.setInt(i + 1, (int) arg);
                        } else if (arg instanceof String) {
                            preparedStatement.setString(i + 1, arg.toString());
                        }
                    }
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return parseResult(resultSet, method.getReturnType(), method);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private Object parseResult(ResultSet rs, Class<?> returnType, Method method) throws Exception {
            if (returnType == List.class) {
                return parseListResult(rs, returnType, method);
            } else {
                return parseSingleResult(rs, returnType);
            }
        }

        private List<Object> parseListResult(ResultSet rs, Class<?> returnType, Method method) throws Exception {
            List<Object> res = new ArrayList<>();
            Class<?> clazz = getListGenericClass(method);
            if (clazz == null) {
                return null;
            }
            do {
                Object o = parseSingleResult(rs, clazz);
                res.add(o);
            } while (rs.next());

            return res;
        }

        private Object parseSingleResult(ResultSet rs, Class<?> returnType) throws Exception {
            Constructor<?> constructor = returnType.getConstructor();
            Object o = constructor.newInstance();
            Field[] declaredFields = returnType.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                String name = declaredField.getName();
                Object column = null;
                if (declaredField.getType() == String.class) {
                    column = rs.getString(name);
                } else if (declaredField.getType() == int.class) {
                    column = rs.getInt(name);
                }
                declaredField.setAccessible(true);
                declaredField.set(o, column);
            }
            return o;
        }

        private String createSelectSql(Method method, Object[] args) {
            Class<?> returnType = method.getReturnType();
            if (returnType == List.class) {
                returnType = getListGenericClass(method);
            }
            List<String> selectColumns = selectColumns(returnType);
            StringBuilder builder = new StringBuilder("SELECT ");
            builder.append(String.join(",", selectColumns));
            builder.append(" FROM ");
            String tableName = getTableName(returnType);
            builder.append(tableName);

            if (args == null) {
                return builder.toString();
            }

            builder.append(" WHERE ");
            String params = getSelectParams(method);
            builder.append(params);
            // String sql = "SELECT id, name, age FROM user WHERE id = ? ";
            return builder.toString();
        }

        private List<String> selectColumns(Class<?> returnType) {
            Field[] declaredFields = returnType.getDeclaredFields();
            return Arrays.stream(declaredFields).map(Field::getName).toList();
        }

        private String getSelectParams(Method method) {
            return Arrays.stream(method.getParameters())
                    .map(parameter -> {
                        Param param = parameter.getAnnotation(Param.class);
                        String value = param.value();
                        return value + " = ?";
                    }).collect(Collectors.joining(" AND "));
        }

        private String getTableName(Class<?> returnType) {
            Table table = returnType.getAnnotation(Table.class);
            if (table == null) {
                throw new RuntimeException("返回值无法确定返回表");
            }
            return table.tableName();
        }

        private Class<?> getListGenericClass(Method method) {
            Type returnType = method.getGenericReturnType();

            if (returnType instanceof ParameterizedType parameterizedType) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    return (Class<?>) actualTypeArguments[0];
                }
            }

            return null;
        }
    }

}
