package com.oracolo.findmycar.telegram.handler.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum TelegramCommand {
  START("/start", "start command"), REGISTER("/register", "register command"), POSITION("/position", "find most recent positon"), BLANK("",
    "blank command"), INVITE("/invite", "invite command");
  private final String command;
  private final String description;

  TelegramCommand(String command, String description) {
    this.command = command;
    this.description = description;
  }

  public static boolean isCommand(String previousText) {
    return Stream.of(values()).map(TelegramCommand::command).anyMatch(s -> s.equals(previousText));
  }

  public static Optional<TelegramCommand> from(String command) {
    try {
      return Optional.of(TelegramCommand.valueOf(command));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public String command() {
    return command;
  }

  public String description() {
    return description;
  }

}
