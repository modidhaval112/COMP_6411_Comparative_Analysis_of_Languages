-module(bank).
-export([startBank/4]).


startBank(BankName, BankBalance, Pid, CustomerMap) -> 
    receive
        {BankName, CustomerName, Aksed_Money, Bank_Keys} ->
            if
                BankBalance >= Aksed_Money ->
                    Pid ! {BankName, 'approves', Aksed_Money, CustomerName},
                    CustomerName ! {Bank_Keys, 'approves', Pid, CustomerMap},
                    Pid ! {BankName, BankBalance - Aksed_Money, "", "", "", "", ""},
                    startBank(BankName, BankBalance - Aksed_Money, Pid, CustomerMap);

                true ->
                    Pid ! {BankName, BankBalance, "", "", "", "", ""},
                    Pid ! {BankName, 'denies', Aksed_Money, CustomerName},
                    lists:delete(BankName, Bank_Keys),
                    CustomerName ! {lists:delete(BankName, Bank_Keys), 'denies', Pid, CustomerMap},
                    startBank(BankName, BankBalance, Pid, CustomerMap)
            end
    end.
    


