package my.app.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import my.app.domains.stock.Stock;
import my.app.domains.user.Tracker;
import my.app.domains.user.User;
import my.app.services.stock.StockService;
import my.app.services.user.TrackerService;
import my.app.services.user.UserService;

@Controller
public class TrackerController {
	
	private final TrackerService trackerService;
	private final UserService userService;
	private final StockService stockService;
	
	@Autowired
	public TrackerController(TrackerService trackerService, UserService userService, StockService stockService) {
		this.trackerService = trackerService;
		this.userService = userService;
		this.stockService = stockService;
	}
	
	@RequestMapping(value = "/tracker", method = RequestMethod.GET)
	public String getTracker(Principal principal, Model model) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		model.addAttribute("trackingList", user.getTrackingList());
		return "tracker";
	}
	
	@RequestMapping(value = "/tracker", method = RequestMethod.POST)
	public String addToTracking(RedirectAttributes ra, Principal principal, Model model,
			@RequestParam(value = "stockId", required = false) Integer stockId,
			@RequestParam(value = "ticker", required = false) String ticker) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		Stock stock;
		if (stockId != null) {
			stock = stockService.getStockById(stockId);
		} else {
			stock = stockService.getStockByTicker(ticker);
			if (stock == null) {
				ra.addFlashAttribute("tickerError", true);
				return "redirect:/stocks";
			}
		}
		List<Tracker> trackingList = user.getTrackingList();
		if (trackingList.stream().anyMatch(o -> o.getStock().getId() == stock.getId())) {
			return "redirect:/tracker";
		}
		Tracker tracker = new Tracker(stock, user);
		trackerService.saveTracker(tracker);
		return "redirect:/tracker";
	}
	
	@RequestMapping(value = "/tracker", method = RequestMethod.DELETE)
	public String removeFromTracking(Principal principal, Model model,
			@RequestParam("id") int id) {
		Tracker tracker = trackerService.getTrackerById(id);
		trackerService.deleteTracker(tracker);
		return "redirect:/tracker";
	}
}
