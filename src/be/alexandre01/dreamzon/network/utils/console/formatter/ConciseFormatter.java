package be.alexandre01.dreamzon.network.utils.console.formatter;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import be.alexandre01.dreamzon.network.utils.console.colors.Colors;
import net.md_5.bungee.api.ChatColor;


public class ConciseFormatter extends Formatter {
        private final DateFormat date = new SimpleDateFormat( System.getProperty( "net.md_5.bungee.log-date-format", "HH:mm:ss" ) );
        private final boolean coloured;

        public ConciseFormatter(boolean coloured) {
            this.coloured = coloured;
        }

        @Override
        @SuppressWarnings("ThrowableResultIgnored")
        public String format(LogRecord record)
        {
            StringBuilder formatted = new StringBuilder();
            formatted.append(Colors.ANSI_PWHITE);
            formatted.append( date.format( record.getMillis() ) );
            formatted.append( Colors.ANSI_BLUE+" ["+ Colors.ANSI_PWHITE);
            if(coloured){
                appendLevel( formatted, record.getLevel() );
            }
            formatted.append(record.getLevel());

            formatted.append( Colors.ANSI_BLUE+"] "+ Colors.ANSI_PWHITE);
            ByteBuffer b = StandardCharsets.UTF_8.encode(formatMessage(record));
            new String(formatMessage( record ).getBytes(), StandardCharsets.UTF_8);
            formatted.append(new String(b.array(), StandardCharsets.UTF_8));
            formatted.append( '\n' );

            if ( record.getThrown() != null )
            {
                StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace( new PrintWriter( writer ) );
                formatted.append( writer );
            }

            return formatted.toString();
        }

        private void appendLevel(StringBuilder builder, Level level)
        {
            if ( !coloured )
            {
                builder.append( level.getLocalizedName() );
                return;
            }

            ChatColor color;

            if ( level == Level.INFO )
            {
                color = ChatColor.BLUE;
            } else if ( level == Level.WARNING )
            {
                color = ChatColor.YELLOW;
            } else if ( level == Level.SEVERE )
            {
                color = ChatColor.RED;
            } else
            {
                color = ChatColor.AQUA;
            }

            builder.append( color ).append( level.getLocalizedName() ).append( ChatColor.RESET );
        }

}
