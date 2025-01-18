USE [BankWebService]
GO
/****** Object:  Table [dbo].[BankAccount]    Script Date: 1/18/2025 11:41:16 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BankAccount](
	[Code] [int] IDENTITY(1,1) NOT NULL,
	[Solde] [decimal](18, 4) NOT NULL,
	[CreationDate] [date] NOT NULL,
 CONSTRAINT [PK_BankAccount] PRIMARY KEY CLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[BankAccount] ADD  CONSTRAINT [DF_BankAccount_CreationDate]  DEFAULT (getdate()) FOR [CreationDate]
GO
/****** Object:  StoredProcedure [dbo].[usp_GetAllBankAccounts]    Script Date: 1/18/2025 11:41:16 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Pierre Abou Serhal
-- Create date: 2025/01/11
-- Description:	Get ALL Bank Accounts
-- =============================================
CREATE PROCEDURE [dbo].[usp_GetAllBankAccounts]
AS
BEGIN
	SET NOCOUNT ON;

    SELECT * FROM BankAccount
END
GO
/****** Object:  StoredProcedure [dbo].[usp_GetBankAccountByCode]    Script Date: 1/18/2025 11:41:16 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Pierre Abou Serhal
-- Create date: 2025/01/11
-- Description:	Get Bank Account By Code
-- =============================================
CREATE PROCEDURE [dbo].[usp_GetBankAccountByCode]
( -- Params
	@Code INT
)
AS
BEGIN
	SET NOCOUNT ON;

    SELECT * FROM BankAccount WHERE Code = @Code
END
GO
/****** Object:  StoredProcedure [dbo].[usp_UpdateBankAccount]    Script Date: 1/18/2025 11:41:16 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		Pierre Abou Serhal
-- Create date: 2025/01/11
-- Description:	Insert / Update a Bank Account
-- =============================================
CREATE PROCEDURE [dbo].[usp_UpdateBankAccount]
( -- Params
	@Code INT,
	@Solde DECIMAL(18, 4),
	@CreationDate Date
)
AS
BEGIN
	SET NOCOUNT ON;

    IF @Code = -1 
	BEGIN
		INSERT INTO BankAccount (Solde, CreationDate) VALUES (@Solde, @CreationDate);

		SET @Code = SCOPE_IDENTITY();
	END

	ELSE
	BEGIN
		UPDATE BankAccount SET Solde = @Solde, CreationDate = @CreationDate WHERE COde = @Code;
	END

	SELECT * FROM BankAccount WHERE Code = @Code;
END
GO
