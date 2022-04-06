package catserver.server.utils;

public class EnumHelper {
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues) {
        return EnumJ17Helper.addEnum(enumType, enumName, paramTypes, paramValues);
    }
}