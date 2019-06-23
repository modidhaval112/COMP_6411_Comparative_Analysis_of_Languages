-module(customer).
-export([startCustomer/6]).


startCustomer(CustomerName, CunstomerMoney, Aksed_Money, Bank_Keys, Pid, CustomerMap) -> 

        if
            length(Bank_Keys) > 0 ->
                Random_Bank = lists:nth(rand:uniform(length(Bank_Keys)), Bank_Keys),
                Pid ! {CustomerName, Aksed_Money, Random_Bank},
                Random_Bank ! {Random_Bank, CustomerName, Aksed_Money, Bank_Keys};
            true ->
                Pid ! {CustomerName, CunstomerMoney, CustomerMap, "",""}
        end,

    receive
        {Updated_Bank_Keys, Decision, Pid, CustomerMap} ->

            if
                CunstomerMoney == 0 ->
                    Pid ! {CustomerName, CustomerMap};
                    
                CunstomerMoney < 50 ->
                    timer:sleep(100),
                    if
                        ((CunstomerMoney - Aksed_Money) =< 0) and (Decision == 'approves') ->
                            Pid ! {CustomerName, CustomerMap};
                        true ->
                            if
                                Decision == 'approves' ->
                                    startCustomer(CustomerName, CunstomerMoney - Aksed_Money, rand:uniform(CunstomerMoney - Aksed_Money), Updated_Bank_Keys, Pid, CustomerMap);
                                true ->
                                    startCustomer(CustomerName, CunstomerMoney, rand:uniform(CunstomerMoney), Updated_Bank_Keys, Pid, CustomerMap)
                            end
                    end;
                true -> 
                    if
                        Decision == 'approves' ->
                            if
                                (CunstomerMoney - Aksed_Money > 50) ->
                                    startCustomer(CustomerName, CunstomerMoney - Aksed_Money, rand:uniform(50), Updated_Bank_Keys, Pid,CustomerMap);
                                ((CunstomerMoney - Aksed_Money =< 50) and (CunstomerMoney - Aksed_Money > 0)) ->
                                    startCustomer(CustomerName, CunstomerMoney - Aksed_Money, rand:uniform(CunstomerMoney - Aksed_Money), Updated_Bank_Keys, Pid, CustomerMap);
                                ((CunstomerMoney - Aksed_Money) =< 0) and (Decision == 'approves') ->
                                    Pid ! {CustomerName, CustomerMap};
                                true ->
                                    void
                            end;
                        true ->
                            startCustomer(CustomerName, CunstomerMoney, rand:uniform(50), Updated_Bank_Keys, Pid, CustomerMap)
                    end
            end
    end.


