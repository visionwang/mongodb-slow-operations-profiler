package de.idealo.mongodb.slowops.command;



import de.idealo.mongodb.slowops.dto.ProfiledServerDto;
import de.idealo.mongodb.slowops.dto.TableDto;
import de.idealo.mongodb.slowops.monitor.MongoDbAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;


public class CommandExecutor implements Callable<TableDto> {

    private static final Logger LOG = LoggerFactory.getLogger(CommandExecutor.class);

    private final ProfiledServerDto profiledServerDto;
    private final ICommand command;

    public CommandExecutor(ProfiledServerDto profiledServerDto, ICommand command) {
        this.profiledServerDto = profiledServerDto;
        this.command = command;
    }


    @Override
    public TableDto call() throws Exception {
    	MongoDbAccessor mongoDbAccessor = null;
    	try {
    		mongoDbAccessor = new MongoDbAccessor(profiledServerDto.getAdminUser(), profiledServerDto.getAdminPw(), profiledServerDto.getHosts());
			return command.runCommand(profiledServerDto, mongoDbAccessor);
		} finally {
			if(mongoDbAccessor!=null) mongoDbAccessor.closeConnections();
		}
    }
}