package com.htmlman1.capitaleconomy.commands.executor;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.htmlman1.capitaleconomy.api.CapitalUserFactory;
import com.htmlman1.capitaleconomy.perms.CapitalPermission;
import com.htmlman1.capitaleconomy.user.CapitalUser;
import com.htmlman1.capitaleconomy.util.ValidationUtils;

public class UserPayCommand {

	public static void execute(CommandSender sender, String[] args) throws IllegalArgumentException {
		if(args.length == 2) {
			if(sender instanceof Player) {
				CapitalUser user = CapitalUserFactory.getUser((Player) sender);
				if(user.hasPermission(CapitalPermission.PAY_OTHERS)) {
					if(CapitalUserFactory.isUser(args[0]) || CapitalUserFactory.isOnline(args[0])) {
						CapitalUser target = CapitalUserFactory.getUser(args[0]);
						if(ValidationUtils.isDouble(args[1])) {
							double amount = Double.parseDouble(args[1]);
							if(user.canAfford(amount)) {
								user.pay(target, amount);
								
								user.sendMessage("�aYou paid �b" + target.getDisplayName() + " �6$" + amount + "�a.");
								target.sendMessage("�aYou received �6$" + amount + " �afrom �b" + user + "�a.");
							} else {
								throw new IllegalArgumentException(CapitalMessages.CANNOT_AFFORD);
							}
						} else {
							throw new IllegalArgumentException(CapitalMessages.USE_NUMBER);
						}
					} else {
						throw new IllegalArgumentException(CapitalMessages.DOES_NOT_EXIST);
					}
				} else {
					throw new IllegalArgumentException(CapitalMessages.NO_PERMS);
				}
			} else {
				if(sender instanceof ConsoleCommandSender) {
					if(CapitalUserFactory.isUser(args[0]) || CapitalUserFactory.isOnline(args[0])) {
						if(ValidationUtils.isDouble(args[1])) {
							MoneyGiveCommand.execute(sender, args);
						} else {
							throw new IllegalArgumentException(CapitalMessages.USE_NUMBER);
						}
					} else {
						throw new IllegalArgumentException(CapitalMessages.DOES_NOT_EXIST);
					}
				}
			}
		} else {
			sender.sendMessage("�c/pay <username> <amount>");
		}
	}
	
}
