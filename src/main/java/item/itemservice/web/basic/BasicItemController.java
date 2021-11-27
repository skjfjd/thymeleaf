package item.itemservice.web.basic;

import item.itemservice.domain.item.Item;
import item.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

   /* @Autowired  // 생략 가능
    public BasicItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
*/

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/item";

    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

   // @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName ,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) { // itemName ,price , quantity 는   addForm.html 에 있는 상품명 name 값.
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);           // set 을 호출하는 이유는 model 떄문
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "/basic/item";
    }
   // @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) {

        itemRepository.save(item);
      //  model.addAttribute("item", item);  어노테이션 모델어트리뷰트는 자동으로 요청파라미터 처리 및 model 자동주입 시켜준다.

        return "/basic/item";
    }
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item ) {
                                         // 요게 소문자로 바뀜
        itemRepository.save(item);
        //  model.addAttribute("item", item);  @ModelAttribute   ->("item) 없어도됨  Item -> item 대문자가 소문자로 바뀜

        return "/basic/item";
    }
    @PostMapping("/add")
    public String addItemV4(Item item ) {  // 객체인경우 modelAttribute 가 자동 주입 된다

        itemRepository.save(item);
        //  model.addAttribute("item", item);

        return "/basic/item";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";

    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
            itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";

    }





    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
