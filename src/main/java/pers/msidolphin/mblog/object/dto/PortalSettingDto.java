package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by msidolphin on 2018/4/8.
 */
@Getter
@Setter
public class PortalSettingDto {

	private Header header;
	private Footer footer;

	@Getter
	@Setter
	public class Header {
		private String avatar;
		private String title;
		private String background;
	}

	@Getter
	@Setter
	public class Footer {
		private Copyright copyright;
		private Design design;
		private List<Contract> contracts;
		private List<PortalTagDto> tags;
		private List<PortalLinkDto> links;
	}

	@Getter
	@Setter
	public class Contract {
		private String name;
		private String value;
		private String icon;
	}

	@Getter
	@Setter
	public class Copyright {
		private String date;
		private String ipc;
		private Site site;
	}

	@Getter
	@Setter
	public class Site {
		private String url;
		private String text;
	}

	@Getter
	@Setter
	public class Design {
		private String name;
		private String value;
	}
}
