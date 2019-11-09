package nkhook.bot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;

public class Main {

	public static void main(String[] args) throws IOException {
		JDABuilder builder = new JDABuilder(Files.readAllLines(new File("A:/Desktop Files/Bloons shit/NKHookBot/NKHookBot/tok.es").toPath()).get(0));
		new GithubWrapper();
		builder.addEventListeners(new MessageListener());
		try {
			builder.build();
		} catch (LoginException e) {
			e.printStackTrace();
		}
	}

}
