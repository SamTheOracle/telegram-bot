package commons;

import com.oracolo.findmycar.telegram.entities.Position;

public class BaseTest {

	protected static Position createPosition(Long chatId, String vehicleName, Integer vehicleId, String latitude, String longitude,
			String timezone, String timestamp, String userId) {
		Position position = new Position();
		position.setVehicleName(vehicleName);
		position.setVehicleId(vehicleId);
		position.setUserId(userId);
		position.setTimezone(timezone);
		position.setLongitude(longitude);
		position.setLatitude(latitude);
		position.setTimeStamp(timestamp);
		position.setChatId(chatId);
		return position;
	}
}
