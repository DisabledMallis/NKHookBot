package nkhook.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{
	public MessageListener() {
		System.out.println("Added listener!");
	}
	
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
		User author = event.getAuthor();
		Message msg = event.getMessage();
		TextChannel chan = (TextChannel) event.getChannel();
		String rawMsg = msg.getContentRaw().toLowerCase();
		Guild guild = event.getGuild();
		Role testers = guild.getRolesByName("Testers", false).get(0);
		Role staff = guild.getRolesByName("Staff", false).get(0);
		Role pings = guild.getRolesByName("Pings", false).get(0);

		if(chan.getName().contains("bot-cmds")) {
			if(!author.isBot()) {
				if(rawMsg.startsWith("nk")) {
					if(rawMsg.substring(2).startsWith("tester")) {
						if(!guild.getMember(author).getRoles().contains(testers)) {
							guild.addRoleToMember(guild.getMember(author), testers).queue();
						}
						else {
							guild.removeRoleFromMember(guild.getMember(author), testers).queue();
						}
					}
					if(rawMsg.substring(2).startsWith("pings")) {
						if(!guild.getMember(author).getRoles().contains(pings)) {
							guild.addRoleToMember(guild.getMember(author), pings).queue();
						}
						else {
							guild.removeRoleFromMember(guild.getMember(author), pings).queue();
						}
					}
					if(rawMsg.substring(2).startsWith("chaninfo")) {
						if(guild.getMember(author).getRoles().contains(staff)) {
							chan.sendMessage("**Welcome to bot commands!**\r\n" + 
									"Messages here will instantly be deleted after being sent by <@642161115547172865>. This means you cannot talk here. In the event the bot is offline and not deleting messages, please, don't talk here!\r\n" + 
									"\r\n" + 
									"Complete list of commands:\r\n" + 
									"```\r\n" + 
									"    - nkTester: Give yourself the tester role\r\n" + 
									"    - nkChanInfo: an administrative command to send this\r\n" +
									"    - nkRepo: the NKHook main github repo\r\n" +
									"    - nkShutdown: turns off the bot. requires staff.\r\n" +
									"    - nkDownload: Get the latest NKHook release\r\n" +
									"```").queue();
						}
					}
					if(rawMsg.substring(2).startsWith("repo")) {
						chan.sendMessage("Github Repository link: https://github.com/DisabledMallis/NKHook5")
							.queue(message -> message.addReaction("U+2705").queue());
					}
					if(rawMsg.substring(2).startsWith("download")) {
						chan.sendMessage(GithubWrapper.getInstance().getLatestDownload())
							.queue(message -> message.addReaction("U+2705").queue());
					}
					if(rawMsg.substring(2).startsWith("shutdown")) {
						if(guild.getMember(author).getRoles().contains(staff)) {
							event.getMessage().delete().queue();
							event.getJDA().shutdown();
							System.exit(0);
						}
					}
				}
			}
			if(!author.getId().contains("642161115547172865")) {
				msg.delete().queue();
			}
		}
    }
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		User author = event.getUser();
		MessageReaction msgR = event.getReaction();
		TextChannel chan = (TextChannel) event.getChannel();
		Message msg = chan.retrieveMessageById(event.getMessageId()).complete();
		
		if(author.isBot()) {
			return;
		}
		if(chan.getName().contains("bot-cmds")) {
			if(msgR.retrieveUsers().complete().contains(msg.getAuthor())) {
				msg.delete().queue();
			}
		}
	}
}
