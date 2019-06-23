-module(money).
-export([start/0, main_thread/4]).
-import(lists,[append/2]). 

start() ->
    {_,CustomerData} = file:consult("customers.txt"),
    {_,BankData} = file:consult("banks.txt"),
    CustomerMap = maps:from_list(CustomerData),
    BankMap = maps:from_list(BankData),
    Customer_Keys = maps:keys(CustomerMap),
    Bank_Keys = maps:keys(BankMap),

    Completed_Cust_list = [], 
    Completed_Bank_list = [], 

    register(pid, spawn(?MODULE, main_thread, [Completed_Cust_list, Completed_Bank_list, Customer_Keys, BankMap])),

    lists:foreach(
      fun(Key) -> 
            #{Key := Val} = CustomerMap,

            if
                (Val < 50) and (Val > 0) ->
                  register(Key, spawn(customer, startCustomer, [Key, Val, rand:uniform(Val), Bank_Keys, pid, CustomerMap]));
                (Val =< 0) ->
                  register(Key, spawn(customer, startCustomer, [Key, Val, 0, Bank_Keys, pid, CustomerMap]));
                true -> 
                  register(Key, spawn(customer, startCustomer, [Key, Val, rand:uniform(50), Bank_Keys, pid, CustomerMap]))
            end
      end,
      Customer_Keys
    ),

    lists:foreach(
      fun(Key) -> 
            #{Key := Val} = BankMap,
            register(Key, spawn(bank, startBank, [Key, Val, pid, CustomerMap]))
      end,
      Bank_Keys
    ),
    timer:sleep(2000).

main_thread(Completed_Cust_list, Completed_Bank_Value_list , Customer_Keys, BankMap) ->

    if
      length(Completed_Cust_list) == length(Customer_Keys) ->
        BankValueMap = maps:from_list(Completed_Bank_Value_list),
        BankValueMap_Keys = maps:keys(BankValueMap),

        lists:foreach(
          fun(Cust_Tuple) -> 
            CustomerName = element(1,Cust_Tuple),
            CustomerMoney = element(2,Cust_Tuple),
            Status = element(3,Cust_Tuple),

            if
              Status == "goal_reached" ->
                io:format("~p has reached the objective of ~p dollar(s). Woo Hoo !!!!!! :) ~n", [CustomerName, CustomerMoney]),
                case lists:member(CustomerName, registered()) of
                  true ->
                    unregister(CustomerName);
                  false ->
                    void
                end;
              true ->
                io:format("~p was only able to borrow ~p dollar(s). Boo Hoo !!!!!! :) ~n", [CustomerName, CustomerMoney]),
                case lists:member(CustomerName, registered()) of
                  true ->
                    unregister(CustomerName);
                  false ->
                    void
                end
            end
          end,
        Completed_Cust_list
        ),

        lists:foreach(
          fun(Key) -> 
            #{Key := Val} = BankValueMap,

            if
                Val /= 0 ->
                    io:format("~p has ~p dollar(s) remaining. ~n", [Key, Val]),
                    case lists:member(Key, registered()) of
                      true ->
                        unregister(Key);
                      false ->
                        void
                    end;
                true -> 
                    io:format("~p has 0 dollar(s) remaining. ~n", [Key]),
                    case lists:member(Key, registered()) of
                      true ->
                        unregister(Key);
                      false ->
                        void
                    end
            end
          end,
        BankValueMap_Keys
        ),
        case lists:member(?MODULE, registered()) of
            true ->
              unregister(?MODULE);
            false ->
              void
          end,
         exit(kill);
      true ->
        void
    end,


    receive 
      {CustomerName, CustomerMoney, CustomerMap, _, _} ->
        main_thread([{CustomerName, maps:get(CustomerName,CustomerMap) - CustomerMoney, "goal_not_reached"} | Completed_Cust_list],Completed_Bank_Value_list, Customer_Keys, BankMap);

      {CustomerName, CustomerMap} ->
        main_thread([{CustomerName, maps:get(CustomerName,CustomerMap), "goal_reached"} | Completed_Cust_list],Completed_Bank_Value_list, Customer_Keys, BankMap);

      {CustomerName, Aksed_Money, Random_Bank} ->
        if
          Aksed_Money /= 0 ->
            io:format("~p requests a loan of ~p dollar(s) from ~p~n" , [CustomerName, Aksed_Money, Random_Bank]);
          true ->
            void
          end,
        main_thread(Completed_Cust_list, Completed_Bank_Value_list, Customer_Keys, BankMap);
      
      {BankName, Status, Aksed_Money, CustomerName} ->
        if
          Aksed_Money /= 0 ->
            io:format("~p ~p a loan of ~p dollars from ~p~n", [BankName, Status, Aksed_Money, CustomerName]);
          true ->
            void
          end,
        main_thread(Completed_Cust_list, Completed_Bank_Value_list, Customer_Keys, BankMap);

      {BankName,BankBalance ,_ ,_ ,_ ,_ ,_ } ->
        case lists:keymember(BankName, 1, Completed_Bank_Value_list) of
            true ->
              main_thread(Completed_Cust_list, [{BankName, BankBalance} | proplists:delete(BankName, Completed_Bank_Value_list)], Customer_Keys, BankMap);
            false ->
              main_thread(Completed_Cust_list, [{BankName, BankBalance} | Completed_Bank_Value_list], Customer_Keys, BankMap)            
        end

    end.


