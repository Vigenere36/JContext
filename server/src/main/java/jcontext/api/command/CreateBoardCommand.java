package jcontext.api.command;

import lombok.Data;

@Data
public class CreateBoardCommand implements Command {
    private final String title;
}
