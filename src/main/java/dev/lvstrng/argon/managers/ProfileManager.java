package dev.lvstrng.argon.managers;

import com.google.gson.*;
import dev.lvstrng.argon.Argon;
import dev.lvstrng.argon.module.Module;
import dev.lvstrng.argon.module.setting.*;
import dev.lvstrng.argon.utils.EncryptedString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ProfileManager {
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private final Path folderPath;
	private String currentProfileName = "argon";

	public ProfileManager() {
		String baseDir = System.getProperty(System.getProperty("os.name").toLowerCase().contains("win")
				? "java.io.tmpdir"
				: "user.home");
		this.folderPath = Paths.get(baseDir, "argon");
	}

	private Path getFilePath(String name) {
		return folderPath.resolve(name + ".json");
	}

	public void loadProfile() {
		loadProfile("argon");
	}

	public void loadProfile(String name) {
		currentProfileName = name;
		Path path = getFilePath(name);
		try {
			if (!Files.isRegularFile(path)) {
				return;
			}
			JsonObject profile = gson.fromJson(Files.readString(path), JsonObject.class);

			for (Module module : Argon.INSTANCE.getModuleManager().getModules()) {
				String moduleName = module.getClass().getSimpleName();
				JsonObject moduleJson = profile.getAsJsonObject(moduleName);
				if (moduleJson == null) continue;

				if (moduleJson.has("enabled") && moduleJson.get("enabled").getAsBoolean()) {
					module.setEnabled(true);
				}

				for (Setting<?> setting : module.getSettings()) {
					String settingName;
					if (setting.getName() instanceof EncryptedString es) {
						settingName = es.toString();
					} else {
						settingName = setting.getName().toString();
					}
					JsonElement settingJson = moduleJson.get(settingName);
					if (settingJson == null) continue;

					applySetting(setting, settingJson, module);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void saveProfile() {
		saveProfile(currentProfileName);
	}



	public void saveProfile(String name) {
		currentProfileName = name;
		Path path = getFilePath(name);
		try {
			Files.createDirectories(folderPath);
			JsonObject profile = new JsonObject();

			for (Module module : Argon.INSTANCE.getModuleManager().getModules()) {
				String moduleName = module.getClass().getSimpleName();
				JsonObject moduleJson = new JsonObject();
				moduleJson.addProperty("enabled", module.isEnabled());

				for (Setting<?> setting : module.getSettings()) {
					String settingName;
					if (setting.getName() instanceof EncryptedString es) {
						settingName = es.toString();
					} else {
						settingName = setting.getName().toString();
					}
					JsonElement value = extractSettingValue(setting);
					if (value != null)
						moduleJson.add(settingName, value);
				}

				profile.add(moduleName, moduleJson);
			}

			Files.writeString(path, gson.toJson(profile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void applySetting(Setting<?> setting, JsonElement json, Module module) {
		try {
			if (setting instanceof BooleanSetting booleanSetting) {
				booleanSetting.setValue(json.getAsBoolean());
			} else if (setting instanceof ModeSetting<?> modeSetting) {
				modeSetting.setModeIndex(json.getAsInt());
			} else if (setting instanceof NumberSetting numberSetting) {
				numberSetting.setValue(json.getAsDouble());
			} else if (setting instanceof KeybindSetting keybindSetting) {
				int key = json.getAsInt();
				keybindSetting.setKey(key);
				module.setKey(key);
			} else if (setting instanceof StringSetting stringSetting) {
				stringSetting.setValue(json.getAsString());
			} else if (setting instanceof MinMaxSetting minMaxSetting && json.isJsonObject()) {
				JsonObject obj = json.getAsJsonObject();
				minMaxSetting.setMinValue(obj.get("1").getAsDouble());
				minMaxSetting.setMaxValue(obj.get("2").getAsDouble());
			}
		} catch (Exception ignored) {
		}
	}


	private JsonElement extractSettingValue(Setting<?> setting) {
		if (setting instanceof BooleanSetting booleanSetting) {
			return new JsonPrimitive(booleanSetting.getValue());
		} else if (setting instanceof ModeSetting<?> modeSetting) {
			return new JsonPrimitive(modeSetting.getModeIndex());
		} else if (setting instanceof NumberSetting numberSetting) {
			return new JsonPrimitive(numberSetting.getValue());
		} else if (setting instanceof KeybindSetting keybindSetting) {
			return new JsonPrimitive(keybindSetting.getKey());
		} else if (setting instanceof StringSetting stringSetting) {
			return new JsonPrimitive(stringSetting.getValue());
		} else if (setting instanceof MinMaxSetting minMaxSetting) {
			JsonObject obj = new JsonObject();
			obj.addProperty("1", minMaxSetting.getMinValue());
			obj.addProperty("2", minMaxSetting.getMaxValue());
			return obj;
		}
		return null;
	}
}