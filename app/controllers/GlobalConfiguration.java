package controllers;


import play.api.Configuration;
import play.api.Play;
import scala.None$;
import scala.Option;

public class GlobalConfiguration {

    private final static GlobalConfiguration INSTANCE = new GlobalConfiguration();

    private int defaultNumberOfIssues;
    private int defaultImplementationSwitch;
    private int actualImplementationSwitch = -1;

    private GlobalConfiguration() {
        super();
    }

    public void init(){
        Option<Configuration> issues_config = Play.configuration(Play.current()).getConfig("issues-number");

        Option<String> defaultIssues = issues_config.get().getString("default", (Option) None$.MODULE$);
        defaultNumberOfIssues = Integer.parseInt(defaultIssues.get());

        Option<String> defaultSwitch = issues_config.get().getString("switch", (Option) None$.MODULE$);
        defaultImplementationSwitch = Integer.parseInt(defaultSwitch.get());
    }


    public static GlobalConfiguration getInstance() {
        return INSTANCE;
    }

    public int getDefaultNumberOfIssues() {
        return defaultNumberOfIssues;
    }


    public int getDefaultImplementationSwitch() {
        return defaultImplementationSwitch;
    }

    public void setActualImplementationSwitch(int number) {
        actualImplementationSwitch = number;
    }


    public int getActualImplementationSwitch() {
        return (actualImplementationSwitch == -1) ? defaultImplementationSwitch : actualImplementationSwitch ;
    }
}
