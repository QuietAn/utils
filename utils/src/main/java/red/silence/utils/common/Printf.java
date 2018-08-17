package red.silence.utils.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Printf {

	public static void printfObj(Object obj) {
		SerializerFeature[] feature = new SerializerFeature[]{
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty,
				};
		
		System.out.println(JSON.toJSONString(obj, feature));
	}
}
