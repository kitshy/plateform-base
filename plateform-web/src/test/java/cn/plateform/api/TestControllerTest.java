package cn.plateform.api;


/**
 * 说明
 *
 * @Description : 描述
 * @Author : songHuaYu
 * @Date: 2020/08/13
 */
public class TestControllerTest {

//    @InjectMocks
//    TestController testController;
//    @Mock
//    private UserMapper userMapper;
//
//
//    @Test
//    public void pubs(){
//        selectUserList();
//        selecctUserListPage();
//        DefaultBackMessage backMessage = testController.pubs();
//        Assert.assertNotNull(backMessage);
//    }
//
//    private void selectUserList(){
//        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), null), User.class);
//        Mockito.when(userMapper.selectList(any(Wrapper.class)))
//                .then(invocationOnMock -> {
//                    List<User> list = new ArrayList<>();
//                    User user = new User();
//                    user.setId(1);
//                    user.setPassword("12345");
//                    user.setUserName("songhuayu");
//                    list.add(user);
//                    return list;
//                });
//    }
//
//    private void selecctUserListPage() {
//        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), null), User.class);
//        Mockito.when(userMapper.selectPage(any(), any(Wrapper.class)))
//                .then(invocation -> {
//                    Page page = invocation.getArgument(0);
//                    Page<User> result = new Page<>();
//                    result.setSize(page.getSize());
//                    result.setTotal(1L);
//                    result.setCurrent(page.getCurrent());
//                    User user = new User();
//                    user.setId(1);
//                    user.setUserName("1234");
//                    user.setPassword("songhauyu");
//                    result.setRecords(Collections.singletonList(user));
//                    return result;
//                });
//    }

}
