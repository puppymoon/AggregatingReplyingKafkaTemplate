package cub.sdd.oneclick.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cub.sdd.oneclick.dto.DataDto;
import cub.sdd.oneclick.dto.DataDto.User;
import cub.sdd.oneclick.dto.UserList;

@Controller
public class IndexController {

	@Autowired
	private TestController testController;

	@RequestMapping("/index")
	public String form(Model model) {
		UserList userList = new UserList();
		String[] topics = new String[] { "jcic", "etch", "intr" };
		model.addAttribute("topics", topics);
		model.addAttribute("userList", userList);
		return "index";
	}

	@ResponseBody
	@PostMapping("/submit")
	public void submit(@RequestBody DataDto dataDto) {
		List<User> list = dataDto.getList();
		testController.sendBatchQuery(list);
	}

}