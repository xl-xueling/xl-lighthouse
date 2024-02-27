import React from 'react';
import { Button, Card, Typography, Space } from '@arco-design/web-react';
import {
  IconArrowRight,
  IconStop,
  IconSwap,
  IconTags,
} from '@arco-design/web-react/icon';
import useLocale from '@/utils/useLocale';
import locale from './locale';

export default function QuickOperation() {
  const t = useLocale(locale);
  return (
    <Card>
      <Space direction="vertical" style={{ width: '100%' }} size={10}>
        <Button long icon={<IconTags />}>
          创建统计项
        </Button>
        <Button long icon={<IconSwap />}>
          修改统计组
        </Button>
        <Button long icon={<IconStop />}>
          查看限流记录
        </Button>
        <Button long icon={<IconArrowRight />}>
          查看秘钥
        </Button>
        <Button long icon={<IconArrowRight />}>
          限流阈值调整
        </Button>
      </Space>
    </Card>
  );
}
